package conf.connections

import java.net.InetAddress

import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy
import com.datastax.driver.core.{PoolingOptions, SocketOptions}
import com.outworkers.phantom.connectors.{ContactPoint, ContactPoints}
import com.outworkers.phantom.dsl._
import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._


object Config {
  val config = ConfigFactory.load()

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

  val masterKeyspaceQuery = KeySpace(Config.masterKeyspace).ifNotExists()
    .`with`(replication eqs NetworkTopologyStrategy
      .data_center(Config.dataCenter, 3))
    .and(durable_writes eqs true)

  val pseudomasterKeyspaceQuery = KeySpace(Config.pseudomasterKeyspace).ifNotExists()
    .`with`(replication eqs NetworkTopologyStrategy
      .data_center(Config.dataCenter, 3))
    .and(durable_writes eqs true)

  val rtViewKeyspaceQuery = KeySpace(Config.rtViewKeyspace).ifNotExists()
    .`with`(replication eqs NetworkTopologyStrategy
      .data_center(Config.dataCenter, 3))
    .and(durable_writes eqs true)

  val batchViewKeyspaceQuery = KeySpace(Config.batchViewKeyspace).ifNotExists()
    .`with`(replication eqs NetworkTopologyStrategy
      .data_center(Config.dataCenter, 3))
    .and(durable_writes eqs true)

  lazy val masterConnector = ContactPoints(Config.hosts, Config.PORT)
    .withClusterBuilder(
      _.withClusterName(Config.clusterName)
        .withSocketOptions(new SocketOptions()
          .setReadTimeoutMillis(Config.readTimeoutMillis)
          .setConnectTimeoutMillis(Config.connectionTimeoutMillis))
        .withPoolingOptions(new PoolingOptions()
          .setMaxQueueSize(100000)
          .setPoolTimeoutMillis(20000))
        .withLoadBalancingPolicy(
          new DCAwareRoundRobinPolicy.Builder()
            .withUsedHostsPerRemoteDc(1)
            .withLocalDc(Config.dataCenter).build()
        )
    ).noHeartbeat().keySpace(Config.masterKeyspace)

  lazy val pseudomasterConnector = ContactPoints(Config.hosts, Config.PORT)
    .withClusterBuilder(
      _.withClusterName(Config.clusterName)
        .withSocketOptions(new SocketOptions()
          .setReadTimeoutMillis(Config.readTimeoutMillis)
          .setConnectTimeoutMillis(Config.connectionTimeoutMillis))
        .withPoolingOptions(new PoolingOptions()
          .setMaxQueueSize(100000)
          .setPoolTimeoutMillis(20000))
        .withLoadBalancingPolicy(
          new DCAwareRoundRobinPolicy.Builder()
            .withUsedHostsPerRemoteDc(1)
            .withLocalDc(Config.dataCenter).build()
        )
    ).noHeartbeat().keySpace(Config.pseudomasterKeyspace)

  lazy val rtViewConnector = ContactPoints(Config.hosts, Config.PORT)
    .withClusterBuilder(
      _.withClusterName(Config.clusterName)
        .withSocketOptions(new SocketOptions()
          .setReadTimeoutMillis(Config.readTimeoutMillis)
          .setConnectTimeoutMillis(Config.connectionTimeoutMillis))
        .withPoolingOptions(new PoolingOptions()
          .setMaxQueueSize(100000)
          .setPoolTimeoutMillis(20000))
        .withLoadBalancingPolicy(
          new DCAwareRoundRobinPolicy.Builder()
            .withUsedHostsPerRemoteDc(1)
            .withLocalDc(Config.dataCenter).build()
        )
    ).noHeartbeat().keySpace(Config.rtViewKeyspace)

  lazy val batchViewConnector = ContactPoints(Config.hosts, Config.PORT)
    .withClusterBuilder(
      _.withClusterName(Config.clusterName)
        .withSocketOptions(new SocketOptions()
          .setReadTimeoutMillis(Config.readTimeoutMillis)
          .setConnectTimeoutMillis(Config.connectionTimeoutMillis))
        .withPoolingOptions(new PoolingOptions()
          .setMaxQueueSize(100000)
          .setPoolTimeoutMillis(20000))
        .withLoadBalancingPolicy(
          new DCAwareRoundRobinPolicy.Builder()
            .withUsedHostsPerRemoteDc(1)
            .withLocalDc(Config.dataCenter).build()
        )
    ).noHeartbeat().keySpace(Config.batchViewKeyspace)

  /**
    * Create an embedded connector, used for testing purposes
    */
//  lazy val testConnector = ContactPoint.embedded.noHeartbeat().keySpace(Config.keySpace)
}
