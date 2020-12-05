package com.imitee.chainblock;

import com.google.gson.GsonBuilder;
import com.imitee.chainblock.entity.Block;
import com.imitee.chainblock.security.Transaction;
import com.imitee.chainblock.security.TransactionOutput;
import com.imitee.chainblock.security.Wallet;
import com.imitee.chainblock.util.StringUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

@SpringBootTest
class ChainblockApplicationTests {

    @Test
    void contextLoads() {

    }
}
