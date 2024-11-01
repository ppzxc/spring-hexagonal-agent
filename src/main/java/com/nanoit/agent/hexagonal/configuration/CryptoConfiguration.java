package com.nanoit.agent.hexagonal.configuration;

import io.github.ppzxc.crypto.Crypto;
import io.github.ppzxc.crypto.CryptoFactory;
import io.github.ppzxc.crypto.CryptoProvider;
import io.github.ppzxc.crypto.Transformation;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.security.Security;

@Configuration
public class CryptoConfiguration {

    private static final byte[] DEFAULT_IV = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00};

    @Bean
    public Crypto crypto () {
        Security.addProvider(new BouncyCastleProvider());
        byte[] keyBytes = new byte[16];
        ByteBuf byteBuf = Unpooled.wrappedBuffer("FqWvV9xzP5wJ4houY76tek1dknrj1".getBytes(StandardCharsets.UTF_8));
        byteBuf.getBytes(0, keyBytes);
        return CryptoFactory.aes(keyBytes, Transformation.AES_CBC_PKCS5PADDING, CryptoProvider.BOUNCY_CASTLE, DEFAULT_IV);
    }
}
