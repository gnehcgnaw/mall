package com.beatshadow.mall.mqtt.iotdb;

import org.apache.iotdb.rpc.IoTDBConnectionException;
import org.apache.iotdb.rpc.StatementExecutionException;
import org.apache.iotdb.session.Session;
import org.apache.iotdb.session.SessionDataSet;
import org.apache.iotdb.tsfile.read.common.RowRecord;

import java.util.List;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/9/3 16:11
 */
public class IoTDBTest {
    public static void main(String[] args) throws IoTDBConnectionException, StatementExecutionException {
        //初始化Session
        Session session = new Session("127.0.0.1", 6667, "root", "root");
        //开启session
        session.open();
        SessionDataSet show_storage_group = session.executeQueryStatement("show storage group");
        while (show_storage_group.hasNext()){
            RowRecord next = show_storage_group.next();
            System.out.println(next);
        }
        SessionDataSet show_timeseries = session.executeQueryStatement("show timeseries");
        while (show_timeseries.hasNext()) {

            System.out.println(show_timeseries.next());
        }
        SessionDataSet sessionDataSet = session.executeQueryStatement("select temperature from root.ln.wf01.wt01 where time < 2017-11-01T00:08:00.000");
        List<String> columnNames = sessionDataSet.getColumnNames();
        List<String> columnTypes = sessionDataSet.getColumnTypes();
        System.out.println(columnNames);
        System.out.println(columnTypes);
        while (sessionDataSet.hasNext()) {
            System.out.println(sessionDataSet.next());
        }

        SessionDataSet sessionDataSet1 = session.executeQueryStatement("select time_difference(status) ,status  from root.ln.wf02.wt02 limit 500000 ");

    }
}
