package com.excalibur.fileapi.ftp;

import com.excalibur.fileapi.configuration.external.FileServerSettings;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.*;
import org.apache.commons.net.ftp.parser.FTPFileEntryParserFactory;
import org.apache.commons.net.io.CopyStreamListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.Charset;

@Component
public class FileClient {

    @Autowired
    private FileServerSettings fileServerSettings;

    private FTPClient ftpClient = new FTPClient();

    public void startNewSession() throws IOException {
        if ( !this.isConnected()) {
            this.connect();

            if (this.isConnected()) {

                if ( !this.login()) {
                    throw new IOException("LOGIN FAILED");
                }
            } else {
                throw new IOException("CONNECT FAILED");
            }
        }
    }

    public void stopSession() throws IOException {
        this.logout();
        this.disconnect();
    }

    public void setDataTimeout(int timeout) {
        ftpClient.setDataTimeout(timeout);
    }

    public void setParserFactory(FTPFileEntryParserFactory parserFactory) {
        ftpClient.setParserFactory(parserFactory);
    }

    public void disconnect() throws IOException {
        ftpClient.disconnect();
    }

    public void setRemoteVerificationEnabled(boolean enable) {
        ftpClient.setRemoteVerificationEnabled(enable);
    }

    public boolean isRemoteVerificationEnabled() {
        return ftpClient.isRemoteVerificationEnabled();
    }

    public boolean login() throws IOException {
        return ftpClient.login(this.fileServerSettings.getUser().getName(), this.fileServerSettings.getUser().getPassword());
    }

    public boolean logout() throws IOException {
        return ftpClient.logout();
    }

    public boolean changeWorkingDirectory(String pathname) throws IOException {
        return ftpClient.changeWorkingDirectory(pathname);
    }

    public boolean changeToParentDirectory() throws IOException {
        return ftpClient.changeToParentDirectory();
    }

    public boolean structureMount(String pathname) throws IOException {
        return ftpClient.structureMount(pathname);
    }

    public boolean reinitialize() throws IOException {
        return ftpClient.reinitialize();
    }

    public void enterLocalActiveMode() {
        ftpClient.enterLocalActiveMode();
    }

    public void enterLocalPassiveMode() {
        ftpClient.enterLocalPassiveMode();
    }

    public boolean enterRemoteActiveMode(InetAddress host, int port) throws IOException {
        return ftpClient.enterRemoteActiveMode(host, port);
    }

    public boolean enterRemotePassiveMode() throws IOException {
        return ftpClient.enterRemotePassiveMode();
    }

    public String getPassiveHost() {
        return ftpClient.getPassiveHost();
    }

    public int getPassivePort() {
        return ftpClient.getPassivePort();
    }

    public int getDataConnectionMode() {
        return ftpClient.getDataConnectionMode();
    }

    public void setActivePortRange(int minPort, int maxPort) {
        ftpClient.setActivePortRange(minPort, maxPort);
    }

    public void setActiveExternalIPAddress(String ipAddress) throws UnknownHostException {
        ftpClient.setActiveExternalIPAddress(ipAddress);
    }

    public void setPassiveLocalIPAddress(String ipAddress) throws UnknownHostException {
        ftpClient.setPassiveLocalIPAddress(ipAddress);
    }

    public void setPassiveLocalIPAddress(InetAddress inetAddress) {
        ftpClient.setPassiveLocalIPAddress(inetAddress);
    }

    public InetAddress getPassiveLocalIPAddress() {
        return ftpClient.getPassiveLocalIPAddress();
    }

    public void setReportActiveExternalIPAddress(String ipAddress) throws UnknownHostException {
        ftpClient.setReportActiveExternalIPAddress(ipAddress);
    }

    public boolean setFileType(int fileType) throws IOException {
        return ftpClient.setFileType(fileType);
    }

    public boolean setFileType(int fileType, int formatOrByteSize) throws IOException {
        return ftpClient.setFileType(fileType, formatOrByteSize);
    }

    public boolean setFileStructure(int structure) throws IOException {
        return ftpClient.setFileStructure(structure);
    }

    public boolean setFileTransferMode(int mode) throws IOException {
        return ftpClient.setFileTransferMode(mode);
    }

    public boolean remoteRetrieve(String filename) throws IOException {
        return ftpClient.remoteRetrieve(filename);
    }

    public boolean remoteStore(String filename) throws IOException {
        return ftpClient.remoteStore(filename);
    }

    public boolean remoteStoreUnique(String filename) throws IOException {
        return ftpClient.remoteStoreUnique(filename);
    }

    public boolean remoteStoreUnique() throws IOException {
        return ftpClient.remoteStoreUnique();
    }

    public boolean remoteAppend(String filename) throws IOException {
        return ftpClient.remoteAppend(filename);
    }

    public boolean completePendingCommand() throws IOException {
        return ftpClient.completePendingCommand();
    }

    public boolean retrieveFile(String remote, OutputStream local) throws IOException {
        return ftpClient.retrieveFile(remote, local);
    }

    public InputStream retrieveFileStream(String remote) throws IOException {
        return ftpClient.retrieveFileStream(remote);
    }

    public boolean storeFile(String remote, InputStream local) throws IOException {
        return ftpClient.storeFile(remote, local);
    }

    public OutputStream storeFileStream(String remote) throws IOException {
        return ftpClient.storeFileStream(remote);
    }

    public boolean appendFile(String remote, InputStream local) throws IOException {
        return ftpClient.appendFile(remote, local);
    }

    public OutputStream appendFileStream(String remote) throws IOException {
        return ftpClient.appendFileStream(remote);
    }

    public boolean storeUniqueFile(String remote, InputStream local) throws IOException {
        return ftpClient.storeUniqueFile(remote, local);
    }

    public OutputStream storeUniqueFileStream(String remote) throws IOException {
        return ftpClient.storeUniqueFileStream(remote);
    }

    public boolean storeUniqueFile(InputStream local) throws IOException {
        return ftpClient.storeUniqueFile(local);
    }

    public OutputStream storeUniqueFileStream() throws IOException {
        return ftpClient.storeUniqueFileStream();
    }

    public boolean allocate(int bytes) throws IOException {
        return ftpClient.allocate(bytes);
    }

    public boolean features() throws IOException {
        return ftpClient.features();
    }

    public String[] featureValues(String feature) throws IOException {
        return ftpClient.featureValues(feature);
    }

    public String featureValue(String feature) throws IOException {
        return ftpClient.featureValue(feature);
    }

    public boolean hasFeature(String feature) throws IOException {
        return ftpClient.hasFeature(feature);
    }

    public boolean hasFeature(String feature, String value) throws IOException {
        return ftpClient.hasFeature(feature, value);
    }

    public boolean allocate(int bytes, int recordSize) throws IOException {
        return ftpClient.allocate(bytes, recordSize);
    }

    public boolean doCommand(String command, String params) throws IOException {
        return ftpClient.doCommand(command, params);
    }

    public String[] doCommandAsStrings(String command, String params) throws IOException {
        return ftpClient.doCommandAsStrings(command, params);
    }

    public FTPFile mlistFile(String pathname) throws IOException {
        return ftpClient.mlistFile(pathname);
    }

    public FTPFile[] mlistDir() throws IOException {
        return ftpClient.mlistDir();
    }

    public FTPFile[] mlistDir(String pathname) throws IOException {
        return ftpClient.mlistDir(pathname);
    }

    public FTPFile[] mlistDir(String pathname, FTPFileFilter filter) throws IOException {
        return ftpClient.mlistDir(pathname, filter);
    }

    public void setRestartOffset(long offset) {
        ftpClient.setRestartOffset(offset);
    }

    public long getRestartOffset() {
        return ftpClient.getRestartOffset();
    }

    public boolean rename(String from, String to) throws IOException {
        return ftpClient.rename(from, to);
    }

    public boolean abort() throws IOException {
        return ftpClient.abort();
    }

    public boolean deleteFile(String pathname) throws IOException {
        return ftpClient.deleteFile(pathname);
    }

    public boolean removeDirectory(String pathname) throws IOException {
        return ftpClient.removeDirectory(pathname);
    }

    public boolean makeDirectory(String pathname) throws IOException {
        return ftpClient.makeDirectory(pathname);
    }

    public String printWorkingDirectory() throws IOException {
        return ftpClient.printWorkingDirectory();
    }

    public boolean sendSiteCommand(String arguments) throws IOException {
        return ftpClient.sendSiteCommand(arguments);
    }

    public String getSystemType() throws IOException {
        return ftpClient.getSystemType();
    }

    public String listHelp() throws IOException {
        return ftpClient.listHelp();
    }

    public String listHelp(String command) throws IOException {
        return ftpClient.listHelp(command);
    }

    public boolean sendNoOp() throws IOException {
        return ftpClient.sendNoOp();
    }

    public String[] listNames(String pathname) throws IOException {
        return ftpClient.listNames(pathname);
    }

    public String[] listNames() throws IOException {
        return ftpClient.listNames();
    }

    public FTPFile[] listFiles(String pathname) throws IOException {
        return ftpClient.listFiles(pathname);
    }

    public FTPFile[] listFiles() throws IOException {
        return ftpClient.listFiles();
    }

    public FTPFile[] listFiles(String pathname, FTPFileFilter filter) throws IOException {
        return ftpClient.listFiles(pathname, filter);
    }

    public FTPFile[] listDirectories() throws IOException {
        return ftpClient.listDirectories();
    }

    public FTPFile[] listDirectories(String parent) throws IOException {
        return ftpClient.listDirectories(parent);
    }

    public FTPListParseEngine initiateListParsing() throws IOException {
        return ftpClient.initiateListParsing();
    }

    public FTPListParseEngine initiateListParsing(String pathname) throws IOException {
        return ftpClient.initiateListParsing(pathname);
    }

    public FTPListParseEngine initiateListParsing(String parserKey, String pathname) throws IOException {
        return ftpClient.initiateListParsing(parserKey, pathname);
    }

    public String getStatus() throws IOException {
        return ftpClient.getStatus();
    }

    public String getStatus(String pathname) throws IOException {
        return ftpClient.getStatus(pathname);
    }

    public String getModificationTime(String pathname) throws IOException {
        return ftpClient.getModificationTime(pathname);
    }

    public FTPFile mdtmFile(String pathname) throws IOException {
        return ftpClient.mdtmFile(pathname);
    }

    public boolean setModificationTime(String pathname, String timeval) throws IOException {
        return ftpClient.setModificationTime(pathname, timeval);
    }

    public void setBufferSize(int bufSize) {
        ftpClient.setBufferSize(bufSize);
    }

    public int getBufferSize() {
        return ftpClient.getBufferSize();
    }

    public void setSendDataSocketBufferSize(int bufSize) {
        ftpClient.setSendDataSocketBufferSize(bufSize);
    }

    public int getSendDataSocketBufferSize() {
        return ftpClient.getSendDataSocketBufferSize();
    }

    public void setReceieveDataSocketBufferSize(int bufSize) {
        ftpClient.setReceieveDataSocketBufferSize(bufSize);
    }

    public int getReceiveDataSocketBufferSize() {
        return ftpClient.getReceiveDataSocketBufferSize();
    }

    public void configure(FTPClientConfig config) {
        ftpClient.configure(config);
    }

    public void setListHiddenFiles(boolean listHiddenFiles) {
        ftpClient.setListHiddenFiles(listHiddenFiles);
    }

    public boolean getListHiddenFiles() {
        return ftpClient.getListHiddenFiles();
    }

    public boolean isUseEPSVwithIPv4() {
        return ftpClient.isUseEPSVwithIPv4();
    }

    public void setUseEPSVwithIPv4(boolean selected) {
        ftpClient.setUseEPSVwithIPv4(selected);
    }

    public void setCopyStreamListener(CopyStreamListener listener) {
        ftpClient.setCopyStreamListener(listener);
    }

    public CopyStreamListener getCopyStreamListener() {
        return ftpClient.getCopyStreamListener();
    }

    public void setControlKeepAliveTimeout(long controlIdle) {
        ftpClient.setControlKeepAliveTimeout(controlIdle);
    }

    public long getControlKeepAliveTimeout() {
        return ftpClient.getControlKeepAliveTimeout();
    }

    public void setControlKeepAliveReplyTimeout(int timeout) {
        ftpClient.setControlKeepAliveReplyTimeout(timeout);
    }

    public int getControlKeepAliveReplyTimeout() {
        return ftpClient.getControlKeepAliveReplyTimeout();
    }

    @Deprecated
    public void setPassiveNatWorkaround(boolean enabled) {
        ftpClient.setPassiveNatWorkaround(enabled);
    }

    public void setPassiveNatWorkaroundStrategy(FTPClient.HostnameResolver resolver) {
        ftpClient.setPassiveNatWorkaroundStrategy(resolver);
    }

    public void setAutodetectUTF8(boolean autodetect) {
        ftpClient.setAutodetectUTF8(autodetect);
    }

    public boolean getAutodetectUTF8() {
        return ftpClient.getAutodetectUTF8();
    }

    @Deprecated
    public String getSystemName() throws IOException {
        return ftpClient.getSystemName();
    }

    public void setControlEncoding(String encoding) {
        ftpClient.setControlEncoding(encoding);
    }

    public String getControlEncoding() {
        return ftpClient.getControlEncoding();
    }

    public int sendCommand(String command, String args) throws IOException {
        return ftpClient.sendCommand(command, args);
    }

    @Deprecated
    public int sendCommand(int command, String args) throws IOException {
        return ftpClient.sendCommand(command, args);
    }

    public int sendCommand(FTPCmd command) throws IOException {
        return ftpClient.sendCommand(command);
    }

    public int sendCommand(FTPCmd command, String args) throws IOException {
        return ftpClient.sendCommand(command, args);
    }

    public int sendCommand(String command) throws IOException {
        return ftpClient.sendCommand(command);
    }

    public int sendCommand(int command) throws IOException {
        return ftpClient.sendCommand(command);
    }

    public int getReplyCode() {
        return ftpClient.getReplyCode();
    }

    public int getReply() throws IOException {
        return ftpClient.getReply();
    }

    public String[] getReplyStrings() {
        return ftpClient.getReplyStrings();
    }

    public String getReplyString() {
        return ftpClient.getReplyString();
    }

    public int user(String username) throws IOException {
        return ftpClient.user(username);
    }

    public int pass(String password) throws IOException {
        return ftpClient.pass(password);
    }

    public int acct(String account) throws IOException {
        return ftpClient.acct(account);
    }

    public int abor() throws IOException {
        return ftpClient.abor();
    }

    public int cwd(String directory) throws IOException {
        return ftpClient.cwd(directory);
    }

    public int cdup() throws IOException {
        return ftpClient.cdup();
    }

    public int quit() throws IOException {
        return ftpClient.quit();
    }

    public int rein() throws IOException {
        return ftpClient.rein();
    }

    public int smnt(String dir) throws IOException {
        return ftpClient.smnt(dir);
    }

    public int port(InetAddress host, int port) throws IOException {
        return ftpClient.port(host, port);
    }

    public int eprt(InetAddress host, int port) throws IOException {
        return ftpClient.eprt(host, port);
    }

    public int pasv() throws IOException {
        return ftpClient.pasv();
    }

    public int epsv() throws IOException {
        return ftpClient.epsv();
    }

    public int type(int fileType, int formatOrByteSize) throws IOException {
        return ftpClient.type(fileType, formatOrByteSize);
    }

    public int type(int fileType) throws IOException {
        return ftpClient.type(fileType);
    }

    public int stru(int structure) throws IOException {
        return ftpClient.stru(structure);
    }

    public int mode(int mode) throws IOException {
        return ftpClient.mode(mode);
    }

    public int retr(String pathname) throws IOException {
        return ftpClient.retr(pathname);
    }

    public int stor(String pathname) throws IOException {
        return ftpClient.stor(pathname);
    }

    public int stou() throws IOException {
        return ftpClient.stou();
    }

    public int stou(String pathname) throws IOException {
        return ftpClient.stou(pathname);
    }

    public int appe(String pathname) throws IOException {
        return ftpClient.appe(pathname);
    }

    public int allo(int bytes) throws IOException {
        return ftpClient.allo(bytes);
    }

    public int feat() throws IOException {
        return ftpClient.feat();
    }

    public int allo(int bytes, int recordSize) throws IOException {
        return ftpClient.allo(bytes, recordSize);
    }

    public int rest(String marker) throws IOException {
        return ftpClient.rest(marker);
    }

    public int mdtm(String file) throws IOException {
        return ftpClient.mdtm(file);
    }

    public int mfmt(String pathname, String timeval) throws IOException {
        return ftpClient.mfmt(pathname, timeval);
    }

    public int rnfr(String pathname) throws IOException {
        return ftpClient.rnfr(pathname);
    }

    public int rnto(String pathname) throws IOException {
        return ftpClient.rnto(pathname);
    }

    public int dele(String pathname) throws IOException {
        return ftpClient.dele(pathname);
    }

    public int rmd(String pathname) throws IOException {
        return ftpClient.rmd(pathname);
    }

    public int mkd(String pathname) throws IOException {
        return ftpClient.mkd(pathname);
    }

    public int pwd() throws IOException {
        return ftpClient.pwd();
    }

    public int list() throws IOException {
        return ftpClient.list();
    }

    public int list(String pathname) throws IOException {
        return ftpClient.list(pathname);
    }

    public int mlsd() throws IOException {
        return ftpClient.mlsd();
    }

    public int mlsd(String path) throws IOException {
        return ftpClient.mlsd(path);
    }

    public int mlst() throws IOException {
        return ftpClient.mlst();
    }

    public int mlst(String path) throws IOException {
        return ftpClient.mlst(path);
    }

    public int nlst() throws IOException {
        return ftpClient.nlst();
    }

    public int nlst(String pathname) throws IOException {
        return ftpClient.nlst(pathname);
    }

    public int site(String parameters) throws IOException {
        return ftpClient.site(parameters);
    }

    public int syst() throws IOException {
        return ftpClient.syst();
    }

    public int stat() throws IOException {
        return ftpClient.stat();
    }

    public int stat(String pathname) throws IOException {
        return ftpClient.stat(pathname);
    }

    public int help() throws IOException {
        return ftpClient.help();
    }

    public int help(String command) throws IOException {
        return ftpClient.help(command);
    }

    public int noop() throws IOException {
        return ftpClient.noop();
    }

    public boolean isStrictMultilineParsing() {
        return ftpClient.isStrictMultilineParsing();
    }

    public void setStrictMultilineParsing(boolean strictMultilineParsing) {
        ftpClient.setStrictMultilineParsing(strictMultilineParsing);
    }

    public boolean isStrictReplyParsing() {
        return ftpClient.isStrictReplyParsing();
    }

    public void setStrictReplyParsing(boolean strictReplyParsing) {
        ftpClient.setStrictReplyParsing(strictReplyParsing);
    }

    public void connect() throws SocketException, IOException {
        ftpClient.connect(this.fileServerSettings.getHostname(), this.fileServerSettings.getPort());
    }

    public boolean isConnected() {
        return ftpClient.isConnected();
    }

    public boolean isAvailable() {
        return ftpClient.isAvailable();
    }

    public void setDefaultPort(int port) {
        ftpClient.setDefaultPort(port);
    }

    public int getDefaultPort() {
        return ftpClient.getDefaultPort();
    }

    public void setDefaultTimeout(int timeout) {
        ftpClient.setDefaultTimeout(timeout);
    }

    public int getDefaultTimeout() {
        return ftpClient.getDefaultTimeout();
    }

    public void setSoTimeout(int timeout) throws SocketException {
        ftpClient.setSoTimeout(timeout);
    }

    public void setSendBufferSize(int size) throws SocketException {
        ftpClient.setSendBufferSize(size);
    }

    public void setReceiveBufferSize(int size) throws SocketException {
        ftpClient.setReceiveBufferSize(size);
    }

    public int getSoTimeout() throws SocketException {
        return ftpClient.getSoTimeout();
    }

    public void setTcpNoDelay(boolean on) throws SocketException {
        ftpClient.setTcpNoDelay(on);
    }

    public boolean getTcpNoDelay() throws SocketException {
        return ftpClient.getTcpNoDelay();
    }

    public void setKeepAlive(boolean keepAlive) throws SocketException {
        ftpClient.setKeepAlive(keepAlive);
    }

    public boolean getKeepAlive() throws SocketException {
        return ftpClient.getKeepAlive();
    }

    public void setSoLinger(boolean on, int val) throws SocketException {
        ftpClient.setSoLinger(on, val);
    }

    public int getSoLinger() throws SocketException {
        return ftpClient.getSoLinger();
    }

    public int getLocalPort() {
        return ftpClient.getLocalPort();
    }

    public InetAddress getLocalAddress() {
        return ftpClient.getLocalAddress();
    }

    public int getRemotePort() {
        return ftpClient.getRemotePort();
    }

    public InetAddress getRemoteAddress() {
        return ftpClient.getRemoteAddress();
    }

    public boolean verifyRemote(Socket socket) {
        return ftpClient.verifyRemote(socket);
    }

    public void setSocketFactory(SocketFactory factory) {
        ftpClient.setSocketFactory(factory);
    }

    public void setServerSocketFactory(ServerSocketFactory factory) {
        ftpClient.setServerSocketFactory(factory);
    }

    public void setConnectTimeout(int connectTimeout) {
        ftpClient.setConnectTimeout(connectTimeout);
    }

    public int getConnectTimeout() {
        return ftpClient.getConnectTimeout();
    }

    public ServerSocketFactory getServerSocketFactory() {
        return ftpClient.getServerSocketFactory();
    }

    public void addProtocolCommandListener(ProtocolCommandListener listener) {
        ftpClient.addProtocolCommandListener(listener);
    }

    public void removeProtocolCommandListener(ProtocolCommandListener listener) {
        ftpClient.removeProtocolCommandListener(listener);
    }

    public void setProxy(Proxy proxy) {
        ftpClient.setProxy(proxy);
    }

    public Proxy getProxy() {
        return ftpClient.getProxy();
    }

    @Deprecated
    public String getCharsetName() {
        return ftpClient.getCharsetName();
    }

    public Charset getCharset() {
        return ftpClient.getCharset();
    }

    public void setCharset(Charset charset) {
        ftpClient.setCharset(charset);
    }
}
