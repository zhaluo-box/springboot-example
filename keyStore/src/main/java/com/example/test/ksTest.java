package com.example.test;

import org.apache.commons.net.util.KeyManagerUtils;

import javax.crypto.SecretKey;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactorySpi;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;

/**
 * @Author: wmz
 * @Description:
 * @Date: 2021/6/21 18:18
 */
public class ksTest {

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        File file = new File("C:\\testDir\\cert\\nest-fftp.pfx");
        InputStream inputStream = new FileInputStream(file);
        char[] passwd = "123456".toCharArray();
        ks.load(inputStream, passwd);
        System.out.println("--------2----------");
        System.out.println(ks.getProvider().toString());
        System.out.println("---------3---------");
        System.out.println(ks.aliases().toString());
        System.out.println("--------4---------");
        System.out.println(ks);
        //        ks.setKeyEntry("123",new byte[]{},passwd,new Certificate[1]);

        Enumeration<String> e = ks.aliases();
        Certificate certificate = null;
        while (e.hasMoreElements()) {
            String entry = e.nextElement();
            System.out.println(entry);
            if (ks.isKeyEntry(entry)) {
                //
                System.out.println(entry);           //return entry;
                certificate = ks.getCertificate(entry);
                System.out.println(certificate);
                Certificate[] certificateChain = ks.getCertificateChain(entry);
                System.out.println(certificateChain);
//                ks.setKeyEntry("aliss", "123456".getBytes(), new Certificate[] { certificate });
                KeyStore.ProtectionParameter protParam =
                                new KeyStore.PasswordProtection("123456".toCharArray());
                KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry)
                                ks.getEntry(entry, protParam);
                PrivateKey myPrivateKey = pkEntry.getPrivateKey();
                System.out.println(myPrivateKey);
                ks.setKeyEntry("aliss",myPrivateKey,passwd,certificateChain);
            }
        }

        Certificate aliss = ks.getCertificate("aliss");
        if (aliss.equals(certificate)) {
            System.out.println("111111111111111111111111111");
        }

        KeyManager clientKeyManager = KeyManagerUtils.createClientKeyManager(ks, null, String.valueOf(passwd));
        System.out.println(clientKeyManager.toString());
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(passwd);
        // get my private key
        KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry("privateKeyAlias", protParam);
        //        PrivateKey myPrivateKey = pkEntry.getPrivateKey();

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
        KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(mySecretKey);
        //   ks.setEntry("secretKeyAlias", skEntry, protParam);

        // store away the keystore
        //        try (FileOutputStream fos = new FileOutputStream("newKeyStoreName")) {
        //            ks.store(fos, "123456".toCharArray());
        //        }
        //        KeyManagerUtils.createClientKeyManager();
    }
}
