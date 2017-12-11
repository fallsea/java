package com.fallsea.id.snowflake;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.I0Itec.zkclient.ZkClient;

/**
 * @Description: id工具类
 * @Copyright: 2017 www.fallsea.com Inc. All rights reserved.
 * @author: fallsea
 * @version 1.0
 * @date: 2017年12月11日 上午10:20:35
 */
public class IdUtil {
	
	/**
	 * zk客户端
	 */
	private static volatile ZkClient zkClient;

	/**
	 * zk根节点
	 */
	public static final String ROOT_PATH = "/fallsea";
	
	
	/**
     * 分布式id节点
     */
    public static final String SNOWFLAKE_ID_PATH = ROOT_PATH + "/SnowflakeId";
	
    /**
     * 可用的工作id集合(0-1023)
     */
	private static volatile Set<Long> WORKER_ID_SET;
    
    private static volatile SnowflakeIdWorker idWorker = null;
    
    /**
     * 当前工作id
     */
    private static volatile long WORKER_ID ;
    
    
    static
    {
    	//实例化zk客户端
    	zkClient = new ZkClient("127.0.0.1:2181",3000,5000);
    	
    	
        WORKER_ID_SET = new HashSet<Long>();
        for (long i = 0; i < 1024; i++)
        {
            WORKER_ID_SET.add(i);
        }
        
        //判断节点是否存在，不存在则创建
        if(!zkClient.exists(SNOWFLAKE_ID_PATH))
        {
            // 创建一个目录节点
        	zkClient.createPersistent(SNOWFLAKE_ID_PATH);
        }
        
        WORKER_ID = getWorkerId();
        
        //通过workerId 计算工作ID 和数据中心
        long workerId = WORKER_ID/32;//工作id
        long datacenterId = WORKER_ID%32;//数据中心
        
        idWorker = new SnowflakeIdWorker(workerId, datacenterId);
        
    }
	
    /**
     * @Description: 获取id
     * @author: fallsea
     * @date: 2017年10月22日 下午8:43:21
     * @return
     */
    public static long getId()
    {
        return idWorker.nextId();
    }
    
    
    /**
     * @Description: 获取workerId(0-1023的值)
     * @author: fallsea
     * @date: 2017年10月22日 下午8:43:52
     * @return
     */
    private static Long getWorkerId()
    {
        
        //查询子节点列表
        List<String> list = zkClient.getChildren(SNOWFLAKE_ID_PATH);
        if(null != list && !list.isEmpty())
        {
            if(list.size()==1024)
            {
                return -1L;
            }
            for (String str : list)
            {
                WORKER_ID_SET.remove(Long.valueOf(str));
            }
        }
        
        long workerId=-1L;
        
        if(!WORKER_ID_SET.isEmpty())
        {
            Long[] workerIds=WORKER_ID_SET.toArray(new Long[]{});
            workerId=workerIds[new Random().nextInt(WORKER_ID_SET.size())];
            
            //判断是否存在，如果不存在，则创建
            if(!zkClient.exists(SNOWFLAKE_ID_PATH+"/"+workerId))
            {
            	zkClient.createEphemeral(SNOWFLAKE_ID_PATH+"/"+workerId);
            }
            else
            {
                return getWorkerId();
            }
        }
        return workerId;
    }
	
}
