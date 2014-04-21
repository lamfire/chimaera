
package com.lamfire.thalia;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.lamfire.thalia.storage.Storage;

import java.io.IOError;
import java.security.spec.KeySpec;

/**
 * Class used to configure and create Thalia. It uses builder pattern.
 */
public class ThaliaBuilder {

    private byte cacheType = RefCachedThalia.MRU;
    private int mruCacheSize = 2048;

    private String location = null;

    private boolean disableTransactions = false;
    private boolean lockingDisabled = false;
    private boolean readonly = false;
    private String password = null;
    private boolean useAES256Bit = true;
    private boolean useRandomAccessFile = false;
    private boolean autoClearRefCacheOnLowMem = true;
    private  boolean closeOnJVMExit = true;
    private  boolean deleteFilesAfterCloseFlag = false;


    private ThaliaBuilder(){}

    /**
     * Creates new DBMaker and sets file to load data from.
     * @param file to load data from
     * @return new ThaliaBuilder
     */
    public static ThaliaBuilder openFile(String file){
        ThaliaBuilder m = new ThaliaBuilder();
        m.location = file;
        return m;
    }

    /**
     * Creates new DBMaker which uses in memory store. Data will be lost after JVM exits.
     * @return new DBMaker
     */
    public static ThaliaBuilder openMemory(){
        return new ThaliaBuilder();
    }

    /**
     * Open store in zip file
     *
     * @param zip file
     * @return new DBMaker
     */
    public static ThaliaBuilder openZip(String zip) {
        ThaliaBuilder m = new ThaliaBuilder();
        m.location = "$$ZIP$$://"+zip;
        return m;
    }

     public static String  isZipFileLocation(String location){
         String match = "$$ZIP$$://";
         if( location.startsWith(match)){
             return location.substring(match.length());
         }
         return null;
     }

    /**
     * Use WeakReference for cache.
     * This cache does not improve performance much,
     * but prevents from creating multiple instances of the same object.
     *
     * @return this builder
     */
    public ThaliaBuilder enableWeakCache() {
        cacheType = RefCachedThalia.WEAK;
        return this;
    }

    /**
     * Use SoftReference for cache.
     * This cache greatly improves performance if you have enoguth memory.
     * Instances in cache are Garbage Collected when memory gets low
     *
     * @return this builder
     */
    public ThaliaBuilder enableSoftCache() {
        cacheType = RefCachedThalia.SOFT;
        return this;
    }

    /**
     * Use hard reference for cache.
     * This greatly improves performance if there is enought memory
     * Hard cache has smaller memory overhead then Soft or Weak, because
     * reference objects and queue does not have to be maintained
     *
     * @return this builder
     */
    public ThaliaBuilder enableHardCache() {
        cacheType = RefCachedThalia.HARD;
        return this;
    }


    /**
     * Use 'Most Recently Used' cache with limited size.
     * Oldest instances are released from cache when new instances are fetched.
     * This cache is not cleared by GC. Is good for systems with limited memory.
     * <p/>
     * Default size for MRU cache is 2048 records.
     *
     * @return this builder
     */
    public ThaliaBuilder enableMRUCache() {
        cacheType = RefCachedThalia.MRU;
        return this;
    }

    /**
     *
     * Sets 'Most Recently Used' cache size. This cache is activated by default with size 2048
     *
     * @param cacheSize number of instances which will be kept in cache.
     * @return this builder
     */
    public ThaliaBuilder setMRUCacheSize(int cacheSize) {
        if (cacheSize < 0) throw new IllegalArgumentException("Cache size is smaller than zero");
        cacheType = RefCachedThalia.MRU;
        mruCacheSize = cacheSize;
        return this;
    }

    /**
     * If reference (soft,weak or hard) cache is enabled,
     * GC may not release references fast enough (or not at all in case of hard cache).
     * So JDBM periodically checks amount of free heap memory.
     * If free memory is less than 25% or 10MB,
     * JDBM completely clears its reference cache to prevent possible memory issues.
     * <p>
     * Calling this method disables auto cache clearing when mem is low.
     * And of course it can cause some out of memory exceptions.
     *
     * @return this builder
     */
    public ThaliaBuilder disableCacheAutoClear(){
        this.autoClearRefCacheOnLowMem = false;
        return this;
    }


    /**
     * Enabled storage encryption using AES cipher. JDBM supports both 128 bit and 256 bit encryption if JRE provides it.
     * There are some restrictions on AES 256 bit and not all JREs have it  by default.
     * <p/>
     * Storage can not be read (decrypted), unless the key is provided next time it is opened
     *
     * @param password used to encrypt store
     * @param useAES256Bit if true strong AES 256 bit encryption is used. Otherwise more usual AES 128 bit is used.
     * @return this builder
     */
    public ThaliaBuilder enableEncryption(String password, boolean useAES256Bit) {
        this.password = password;
        this.useAES256Bit = useAES256Bit;
        return this;
    }




    /**
     * Make DB readonly.
     * Update/delete/insert operation will throw 'UnsupportedOperationException'
     *
     * @return this builder
     */
    public ThaliaBuilder readonly() {
        readonly = true;
        return this;
    }


    /**
     * Disable cache completely
     *
     * @return this builder
     */
    public ThaliaBuilder disableCache() {
        cacheType = RefCachedThalia.NONE;
        return this;
    }


    /**
     * Option to disable transaction (to increase performance at the cost of potential data loss).
     * Transactions are enabled by default
     * <p/>
     * Switches off transactioning for the record manager. This means
     * that a) a transaction log is not kept, and b) writes aren't
     * synch'ed after every update. Writes are cached in memory and then flushed
     * to disk every N writes. You may also flush writes manually by calling commit().
     * This is useful when batch inserting into a new database.
     * <p/>
     * When using this, database must be properly closed before JVM shutdown.
     * Failing to do so may and WILL corrupt store.
     *
     * @return this builder
     */
    public ThaliaBuilder disableTransactions() {
        this.disableTransactions = true;
        return this;
    }
    
    /**
     * Disable file system based locking (for file systems that do not support it).
     * 
     * Locking is not supported by many remote or distributed file systems; such
     * as Lustre and NFS. Attempts to perform locks will result in an 
     * IOException with the message "Function not implemented".
     * 
     * Disabling locking will avoid this issue, though of course it comes with
     * all the issues of uncontrolled file access.
     * 
     * @return this builder
     */
    public ThaliaBuilder disableLocking(){
        this.lockingDisabled = true;
        return this;
    }
    
    /**
     * By default JDBM uses mapped memory buffers to read from files.
     * But this may behave strangely on some platforms.
     * Safe alternative is to use old RandomAccessFile rather then mapped ByteBuffer.
     * There is typically slower (pages needs to be copyed into memory on every write).
     *
     * @return this builder
     */
    public ThaliaBuilder useRandomAccessFile(){
        this.useRandomAccessFile = true;
        return this;
    }


    /**
     * Registers shutdown hook and close database on JVM exit, if it was not already closed; 
     * 
     * @return this builder
     */
    public ThaliaBuilder closeOnExit(){
        this.closeOnJVMExit = true;
        return this;
    }

    /**
     * Delete all storage files after DB is closed
     *
     * @return this builder
     */
    public ThaliaBuilder deleteFilesAfterClose(){
        this.deleteFilesAfterCloseFlag = true;
        return this;
    }

    /**
     * Opens database with settings earlier specified in this builder.
     *
     * @return new DB
     * @throws java.io.IOError if db could not be opened
     */
    public Thalia build() {

        Cipher cipherIn = null;
        Cipher cipherOut = null;
        if (password != null) try {
            //initialize ciphers
            //this code comes from stack owerflow
            //http://stackoverflow.com/questions/992019/java-256bit-aes-encryption/992413#992413
            byte[] salt = new byte[]{3, -34, 123, 53, 78, 121, -12, -1, 45, -12, -48, 89, 11, 100, 99, 8};

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 1024, useAES256Bit?256:128);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            String transform = "AES/CBC/NoPadding";
            IvParameterSpec params = new IvParameterSpec(salt);

            cipherIn = Cipher.getInstance(transform);
            cipherIn.init(Cipher.ENCRYPT_MODE, secret, params);

            cipherOut = Cipher.getInstance(transform);
            cipherOut.init(Cipher.DECRYPT_MODE, secret, params);

            //sanity check, try with page size
            byte[] data = new byte[Storage.PAGE_SIZE];
            byte[] encData = cipherIn.doFinal(data);
            if (encData.length != Storage.PAGE_SIZE)
                throw new Error("Page size changed after encryption, make sure you use '/NoPadding'");
            byte[] data2 = cipherOut.doFinal(encData);
            for (int i = 0; i < data.length; i++) {
                if (data[i] != data2[i]) throw new Error("Encryption provided by JRE does not work");
            }

        } catch (Exception e) {
            throw new IOError(e);
        }

        ThaliaBase db = null;


        if (cacheType == RefCachedThalia.MRU){
          db = new MRUCachedThalia(location, readonly, disableTransactions, cipherIn, cipherOut,useRandomAccessFile,deleteFilesAfterCloseFlag, mruCacheSize,lockingDisabled);
        }else if( cacheType == RefCachedThalia.SOFT || cacheType == RefCachedThalia.HARD || cacheType == RefCachedThalia.WEAK) {
            db = new RefCachedThalia(location, readonly, disableTransactions, cipherIn, cipherOut,useRandomAccessFile,deleteFilesAfterCloseFlag, cacheType,autoClearRefCacheOnLowMem,lockingDisabled);
        } else if (cacheType == RefCachedThalia.NONE) {
            db = new ThaliaStore(location, readonly, disableTransactions, cipherIn, cipherOut,useRandomAccessFile,deleteFilesAfterCloseFlag,lockingDisabled);
        } else {
            throw new IllegalArgumentException("Unknown cache type: " + cacheType);
        }
        
        if(closeOnJVMExit){
            db.addShutdownHook();
        }

        return db;
    }

}
