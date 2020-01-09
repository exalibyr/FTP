package com.excalibur.ftp.util;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class FTPNoOpThread implements Runnable {

    private FTPClient ftpClient;
    private ReentrantLock lock;
    private Boolean isAlive;

    public FTPNoOpThread(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
        this.lock = new ReentrantLock();
        this.isAlive = true;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public void setAlive(Boolean alive) {
        isAlive = alive;
    }

    @Override
    public void run() {
        try {
            while (isAlive) {
                Thread.sleep(30000);
                if (lock.tryLock(0, TimeUnit.SECONDS)) {
                    ftpClient.sendNoOp();
                    lock.unlock();
                }
            }
        } catch (Exception e) {
            lock.unlock();
            e.printStackTrace();
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }
}
