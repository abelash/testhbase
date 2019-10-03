package tsthbase

import org.apache.spark._
//import org.apache.hadoop.hbase


object tsthbasespark {
// https://blog.cloudera.com/spark-hbase-dataframe-based-hbase-connector/


//Define the catalog for the schema mapping
def catalog = s"""{
                 |"table":{"namespace":"default", "name":"table1"},
                 |"rowkey":"key",
                 |"columns":{
                 |"col0":{"cf":"rowkey", "col":"key", "type":"string"},
                 |"col1":{"cf":"cf1", "col":"col1", "type":"boolean"},
                 |"col2":{"cf":"cf2", "col":"col2", "type":"double"},
                 |"col3":{"cf":"cf3", "col":"col3", "type":"float"},
                 |"col4":{"cf":"cf4", "col":"col4", "type":"int"},
                 |"col5":{"cf":"cf5", "col":"col5", "type":"bigint"},
                 |"col6":{"cf":"cf6", "col":"col6", "type":"smallint"},
                 |"col7":{"cf":"cf7", "col":"col7", "type":"string"},
                 |"col8":{"cf":"cf8", "col":"col8", "type":"tinyint"}
                 |}
                 |}""".stripMargin

//2) Prepare the data and populate the HBase table
case class HBaseRecord(col0: String, col1: Boolean,col2: Double, col3: Float,col4: Int,       col5: Long, col6: Short, col7: String, col8: Byte)

object HBaseRecord {
  def apply(i: Int, t: String): HBaseRecord = {
    val s = s"row${"%03d".format(i)}"
    HBaseRecord(s, i % 2 == 0, i.toDouble, i.toFloat,  i, i.toLong, i.toShort,  s"String$i: $t",      i.toByte)
  }
}


val data = (0 to 255).map { i =>  HBaseRecord(i, "extra")}

/*
sc.parallelize(data).toDF.write.options(
  Map(HBaseTableCatalog.tableCatalog -> catalog, HBaseTableCatalog.newTable -> "5"))
.format("org.apache.spark.sql.execution.datasources.hbase")
.save()


def withCatalog(cat: String): DataFrame = {
  sqlContext
    .read
    .options(Map(HBaseTableCatalog.tableCatalog -> cat))
    .format(
  " org.apache.spark.sql.execution.datasources.hbase
  ")
  .load()

//3) Load the DataFrame:
  def withCatalog(cat: String): DataFrame = {
    sqlContext
      .read
      .options(Map(HBaseTableCatalog.tableCatalog -> cat))
      .format("org.apache.spark.sql.execution.datasources.hbase")
    .load()
  }

  val df = withCatalog(catalog)

  //4) Language integrated query:
  val s = df.filter((($"col0" <= "row050" && $"col0" > "row040") ||
  $"col0" === "row005" ||
  $"col0" === "row020" ||
  $"col0" ===  "r20" ||
  $"col0" <= "row005") &&
  ($"col4" === 1 ||
    $"col4" === 42))
  .select("col0", "col1", "col4")
  s.show  
  
  //5) SQL query:
  df.registerTempTable("table")
  sqlContext.sql(
  " select count (col1) from table
  ").show

  /*
Configuring Spark-Package
Users can use the Spark-on-HBase connector as a standard Spark package. To include the package in your Spark application use:

spark-shell, pyspark, or spark-submit

> $SPARK_HOME/bin/spark-shell –packages zhzhan:shc:0.0.11-1.6.1-s_2.10

Users can include the package as the dependency in your SBT file as well. The format is the spark-package-name:version

spDependencies += "zhzhan/shc:0.0.11-1.6.1-s_2.10"
*/

 */
}