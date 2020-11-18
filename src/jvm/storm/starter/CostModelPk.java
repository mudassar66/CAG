/*    */ package storm.starter;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import org.apache.storm.Config;
/*    */ import org.apache.storm.LocalCluster;
/*    */ import org.apache.storm.StormSubmitter;
/*    */ import org.apache.storm.generated.StormTopology;
/*    */ import org.apache.storm.topology.BoltDeclarer;
/*    */ import org.apache.storm.topology.TopologyBuilder;
/*    */ import org.apache.storm.tuple.Fields;
/*    */ import org.apache.storm.utils.Utils;
/*    */ 
/*    */ public class CostModelPk
/*    */ {
/*    */   public static final String KAFKA_SPOUT_ID = "kafka-spout";
/*    */   public static final String AGGREGATOR_BOLT_ID = "aggregator-bolt";
/*    */   public static final String WORDCOUNTER_BOLT_ID = "wordcountter-bolt";
/*    */   public static final String TOPOLOGY_NAME = "keyGroupingBalancing-topology";
/*    */ 
/*    */   public static void main(String[] args)
/*    */     throws Exception
/*    */   {
/*    */     try
/*    */     {
/* 61 */        TopologyBuilder builder = new TopologyBuilder();
/* 62 */        Integer spouts = Integer.valueOf(args[1]);
/* 63 */        Integer bolt_counts = Integer.valueOf(args[2]);
               Integer bolt_agg = Integer.valueOf(args[3]);
               Integer is_aggregation_step = Integer.valueOf(args[4]);
               Integer tuples_to_emit = Integer.valueOf(args[5]);
               Integer number_of_workers = Integer.valueOf(args[6]); 
               Integer ackers = Integer.valueOf(args[7]); 
               Integer sleep_time = Integer.valueOf(args[8]); 
               Double skew = Double.valueOf(args[9]); 
               String file_name = args[10]; 
               String ds = args[11]; 
               
/* 69 */       if (is_aggregation_step == 0  && skew>0 ){
                     builder.setSpout("kafka-spout", new DataReaderSpout(args[0], spouts, bolt_counts,bolt_agg, is_aggregation_step, tuples_to_emit), spouts);
                     builder.setBolt("call-log-counter-bolt",new DataCounterBolt(args[0], spouts, bolt_counts,bolt_agg, is_aggregation_step, tuples_to_emit,skew,ackers), bolt_counts).partialKeyGrouping("kafka-spout", new Fields(new String[] { "word" }));
                   
                }else if(is_aggregation_step == 0 && skew == 0 ){
                    
                    builder.setSpout("kafka-spout", new DataReaderSpout1(args[0], spouts, bolt_counts,bolt_agg, is_aggregation_step, tuples_to_emit,ackers,sleep_time,file_name,ds), spouts);
                     builder.setBolt("call-log-counter-bolt",new DataCounterBolt(args[0], spouts, bolt_counts,bolt_agg, is_aggregation_step, tuples_to_emit,skew,ackers), bolt_counts).partialKeyGrouping("kafka-spout", new Fields(new String[] { "word" }));

                } else{
                      builder.setSpout("kafka-spout", new DataReaderSpout1(args[0], spouts, bolt_counts,bolt_agg, is_aggregation_step, tuples_to_emit,ackers,sleep_time,file_name,ds), spouts);
                     builder.setBolt("call-log-counter-bolt", new DataCounterBolt(args[0], spouts, bolt_counts,bolt_agg, is_aggregation_step, tuples_to_emit,skew,ackers), bolt_counts).partialKeyGrouping("kafka-spout", new Fields(new String[] { "word" }));
                    builder.setBolt("call-log-agg-bolt", new MyAggregator(args[0], spouts, bolt_counts,bolt_agg, is_aggregation_step, tuples_to_emit,ackers), bolt_agg).partialKeyGrouping("call-log-counter-bolt", new Fields(new String[] { "word" }));

           
                }


/* 84 */       Config config = new Config();
/* 85 */     //  config.put("topology.message.timeout.secs", Integer.valueOf(60));
/*    */       config.setNumAckers(ackers);
/* 87 */       config.setDebug(false);
/*    */ 
/* 89 */       if ((args != null) && (args.length > 0) && (!args[0].equals("0")))
/*    */       {
/* 91 */         config.setNumWorkers(number_of_workers.intValue());
/* 92 */         StormSubmitter.submitTopologyWithProgressBar(args[0], config, builder.createTopology());
/*    */       }
/*    */       else {
/* 95 */         LocalCluster cluster = new LocalCluster();
/* 96 */         StormTopology topology = builder.createTopology();
/* 97 */         cluster.submitTopology("CostModel", config, topology);
/* 98 */         Utils.sleep(600000L);
/* 99 */         cluster.killTopology("CostModel");
/* 100 */         cluster.shutdown();
/*    */       }
/*    */     }
/*    */     catch (Exception e) {
/* 104 */       System.out.println("Exception in producer");
/* 105 */       System.out.println(e.toString());
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\mudassar\phd\HUST\Prof Chen\experiments\storm\paritioning algo paper\files\storm-starter-1.2.2.jar
 * Qualified Name:     storm.starter.CostModelPk
 * JD-Core Version:    0.6.2
 */