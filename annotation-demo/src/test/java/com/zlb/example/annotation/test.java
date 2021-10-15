package com.zlb.example.annotation;

import org.apache.commons.net.util.KeyManagerUtils;

import javax.crypto.SecretKey;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactorySpi;
import javax.net.ssl.KeyStoreBuilderParameters;
import javax.net.ssl.ManagerFactoryParameters;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * @Author: wmz
 * @Description:
 * @Date: 2021/6/21 17:25
 */
public class test {

    public static void main(String[] args) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableEntryException {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        File file = new File("C:\\testDir\\cert\\nest-ftp.pfx");
        InputStream inputStream = new FileInputStream(file);
        ks.load(inputStream,"123456".toCharArray());
        ks.toString();
//        KeyManagerUtils.createClientKeyManager(ks,null,)

        KeyStore.ProtectionParameter protParam =
                        new KeyStore.PasswordProtection("123456".toCharArray());
        // get my private key
        KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry)
                        ks.getEntry("privateKeyAlias", protParam);
        PrivateKey myPrivateKey = pkEntry.getPrivateKey();

        // save my secret key
        javax.crypto.SecretKey mySecretKey = new SecretKey() {
            @Override
            public String getAlgorithm() {
                return null;
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public byte[] getEncoded() {
                return new byte[0];
            }
        };
        KeyStore.SecretKeyEntry skEntry =
                        new KeyStore.SecretKeyEntry(mySecretKey);
        ks.setEntry("secretKeyAlias", skEntry, protParam);

        // store away the keystore
//        try (FileOutputStream fos = new FileOutputStream("newKeyStoreName")) {
//            ks.store(fos, "123456".toCharArray());
//        }
    }
}
