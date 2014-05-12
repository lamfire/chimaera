package com.lamfire.chimaera.test.filestore.benchmark;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-23
 * Time: 下午3:22
 * To change this template use File | Settings | File Templates.
 */
public class DiskFireQueueReadTest {
    public static void main(String[] args) throws Exception {
        DiskFireQueueBenchmark.startupReadThreads(5);
    }
}
