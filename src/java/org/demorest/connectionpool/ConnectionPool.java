/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demorest.connectionpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * ConnectionPool class is a singleton.<br>
 * In order to implement this class the following variables are used:
 <ul>
 * <li>private final static String JDBC_DRIVER = "com.mysql.jdbc.Driver"</li>
 * <li>private final static String DB_URL = "jdbc:mysql://localhost/marvinstud?characterEncoding=utf8"</li>
 * <li>private final static String USER = "MarvinStud"</li>
 * <li>private final static String PASS = " "</li>
 * <li>private final  {@code ArrayList<Connection>} ConnectionPool</li>
 * <li>private static final int INITIAL_POOL_SIZE = 50</li>
 * <li>private static ConnectionPool inst = null</li>
 * </ul>
 * @author George
 */
public class ConnectionPool {

    private final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final static String DB_URL = "jdbc:mysql://localhost/marvinstud?characterEncoding=utf8";
    private final static String USER = "MarvinStud";
    private final static String PASS = " ";
    private final ArrayList<Connection> ConnectionPool;
    private static final int INITIAL_POOL_SIZE = 50;
    
    private static ConnectionPool inst = null;

    /**
     * Private constructor.Fills the ConnectionPool list with connections,ConnectionPool size = INITIAL_POOL_SIZE.
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    private ConnectionPool() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        ConnectionPool = new  ArrayList<>(INITIAL_POOL_SIZE); 
        createConnections(); 
    }

    /**
     * This method is used to return a Connection from the ConnectionPool ArrayList.<br>
     * if the ConenctionPool is Empty then is filled again with Connections using createConnections() method.<br>
     * While searching for valid connections ,if some connections are not valid then these ones are removed from the ConnectionPool ArrayList.<br>
     * if there are not valid connections at all, then the createConnections method is called again.
     * @return Connection. A valid connection.
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */

    public synchronized  Connection getConnection() throws ClassNotFoundException, SQLException {
       boolean flag = false;
       //check if ArrayList is empty
       if(ConnectionPool.isEmpty()){
            createConnections();
       }
       Connection con =null;
       //searching for valid connection to return
       while(!ConnectionPool.isEmpty()){
           con = ConnectionPool.get(0);
           ConnectionPool.remove(con);
           if(con.isValid(0)){
              flag = true;
           }  
       }
       //check if there is any valid connection
       //if not execute createConnections() and return a connection
       if(flag == false){
           createConnections();
           Connection con1 = ConnectionPool.get(0);
           ConnectionPool.remove(con1);
           return con1;
       }else
            return con; 
    }

    /**
     * When this method is called the connection gets added back to the ConnectionPool.<br>
     * The connection gets added back only if it is valied ,in other case the connection doesn't gets added.
     * @param con Connection. The Connection that gets added back to the ConnectionPool.
     * @throws java.sql.SQLException
     */

    public synchronized  void releaseConnection(Connection con) throws SQLException {
        if(con.isValid(0)){
             ConnectionPool.add(con);
        }
    }

    /**
     * This method is used in order to fill the ConnectionPool with Connection objects.
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
   private void createConnections() throws ClassNotFoundException, SQLException{
         for(int i=0; i< INITIAL_POOL_SIZE; i++){
            ConnectionPool.add(DriverManager.getConnection(DB_URL,USER,PASS));
        }
    }
    /**
     * This method is used for debugging reasons mostly.<br>
     * Displays the ConnectionPool size.<br>
     */
    public void display() {
       System.out.println(ConnectionPool.size());
    }
    
    /**
     * This method is used in order to use this class as a singleton.<br>
     * it checks if a ConnectionPool object has been instantiated.<br>
     * if an object has not been instantiated then it gets instantiated and returned.<br>
     * if the object has been already instantiated then it simply returns the ConnectionPool object.<br>
     * @return ConnectionPool. The ConnectionPool
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public synchronized static ConnectionPool getInstance() throws ClassNotFoundException, SQLException{
        if(inst == null)
           inst = new ConnectionPool(); 
        return inst;
    }
    
}
