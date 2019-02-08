package conf.connections

import java.net.InetAddress

import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy
import com.datastax.driver.core.{PoolingOptions, SocketOptions}
import com.outworkers.phantom.connectors.ContactPoints
import com.outworkers.phantom.dsl._
import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters._


object Configuration {
  val config: Config = ConfigFactory.load()

  def PORT = 9042

  def connectionTimeoutMillis = 700000

  // Default is 5000
  def readTimeoutMillis = 1500000

  // Default is 12000
  val hosts: Seq[String] = config.getStringList("cassandra.host").asScala.toList
  val dataCenter = config.getString("cassandra.dataCenter")
  val inets = hosts.map(InetAddress.getByName)
  val masterKeyspace: String = config.getString("cassandra.masterKeyspace")
  val pseudomasterKeyspace: String = config.getString("cassandra.pseudomasterKeyspace")
  val rtViewKeyspace: String = config.getString("cassandra.rtViewKeyspace")
  val batchViewKeyspace: String = config.getString("cassandra.batchViewKeyspace")
  val clusterName: String = config.getString("cassandra.clusterName")
}

object DataConnection {

  val masterKeyspaceQuery = KeySpace(Configuration.masterKeyspace).ifNotExists()
    .`with`(replication eqs NetworkTopologyStrategy
      .data_center(Configuration.dataCenter, 3))
    .and(durable_writes eqs true)

  val pseudomasterKeyspaceQuery = KeySpace(Configuration.pseudomasterKeyspace).ifNotExists()
    .`with`(replication eqs NetworkTopologyStrategy
      .data_center(Configuration.dataCenter, 3))
    .and(durable_writes eqs true)

  val rtViewKeyspaceQuery = KeySpace(Configuration.rtViewKeyspace).ifNotExists()
    .`with`(replication eqs NetworkTopologyStrategy
      .data_center(Configuration.dataCenter, 3))
    .and(durable_writes eqs true)

  val batchViewKeyspaceQuery = KeySpace(Configuration.batchViewKeyspace).ifNotExists()
    .`with`(replication eqs NetworkTopologyStrategy
      .data_center(Configuration.dataCenter, 3))
    .and(durable_writes eqs true)

  lazy val masterConnector = ContactPoints(Configuration.hosts, Configuration.PORT)
    .withClusterBuilder(
      _.withClusterName(Configuration.clusterName)
        .withSocketOptions(new SocketOptions()
          .setReadTimeoutMillis(Configuration.readTimeoutMillis)
          .setConnectTimeoutMillis(Configuration.connectionTimeoutMillis))
        .withPoolingOptions(new PoolingOptions()
          .setMaxQueueSize(100000)
          .setPoolTimeoutMillis(20000))
        .withLoadBalancingPolicy(
          new DCAwareRoundRobinPolicy.Builder()
            .withUsedHostsPerRemoteDc(1)
            .withLocalDc(Configuration.dataCenter).build()
        )
    ).noHeartbeat().keySpace(Configuration.masterKeyspace)

  lazy val pseudomasterConnector = ContactPoints(Configuration.hosts, Configuration.PORT)
    .withClusterBuilder(
      _.withClusterName(Configuration.clusterName)
        .withSocketOptions(new SocketOptions()
          .setReadTimeoutMillis(Configuration.readTimeoutMillis)
          .setConnectTimeoutMillis(Configuration.connectionTimeoutMillis))
        .withPoolingOptions(new PoolingOptions()
          .setMaxQueueSize(100000)
          .setPoolTimeoutMillis(20000))
        .withLoadBalancingPolicy(
          new DCAwareRoundRobinPolicy.Builder()
            .withUsedHostsPerRemoteDc(1)
            .withLocalDc(Configuration.dataCenter).build()
        )
    ).noHeartbeat().keySpace(Configuration.pseudomasterKeyspace)

  lazy val rtViewConnector = ContactPoints(Configuration.hosts, Configuration.PORT)
    .withClusterBuilder(
      _.withClusterName(Configuration.clusterName)
        .withSocketOptions(new SocketOptions()
          .setReadTimeoutMillis(Configuration.readTimeoutMillis)
          .setConnectTimeoutMillis(Configuration.connectionTimeoutMillis))
        .withPoolingOptions(new PoolingOptions()
          .setMaxQueueSize(100000)
          .setPoolTimeoutMillis(20000))
        .withLoadBalancingPolicy(
          new DCAwareRoundRobinPolicy.Builder()
            .withUsedHostsPerRemoteDc(1)
            .withLocalDc(Configuration.dataCenter).build()
        )
    ).noHeartbeat().keySpace(Configuration.rtViewKeyspace)

  lazy val batchViewConnector = ContactPoints(Configuration.hosts, Configuration.PORT)
    .withClusterBuilder(
      _.withClusterName(Configuration.clusterName)
        .withSocketOptions(new SocketOptions()
          .setReadTimeoutMillis(Configuration.readTimeoutMillis)
          .setConnectTimeoutMillis(Configuration.connectionTimeoutMillis))
        .withPoolingOptions(new PoolingOptions()
          .setMaxQueueSize(100000)
          .setPoolTimeoutMillis(20000))
        .withLoadBalancingPolicy(
          new DCAwareRoundRobinPolicy.Builder()
            .withUsedHostsPerRemoteDc(1)
            .withLocalDc(Configuration.dataCenter).build()
        )
    ).noHeartbeat().keySpace(Configuration.batchViewKeyspace)

  /**
    * Create an embedded connector, used for testing purposes
    */
  //  lazy val testConnector = ContactPoint.embedded.noHeartbeat().keySpace(Config.keySpace)
}
