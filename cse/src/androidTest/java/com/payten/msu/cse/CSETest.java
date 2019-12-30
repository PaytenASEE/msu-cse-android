package com.payten.msu.cse;

import androidx.core.util.Consumer;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Created by jasmin.suljic@monri.com
 * MSU CSE
 */
@RunWith(AndroidJUnit4.class)
public class CSETest {

    private CSE cse;

    @Before
    public void setUp() throws Exception {
        cse = new CSE(true);
    }

    @Test
    public void encrypt() throws InterruptedException {
        cse.encrypt("4111 1111 1111 1111",
                    "John the Tester",
                    2021,
                    10,
                    "123",
                    "123456",
                    new EncryptCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Assert.assertNotNull(result);
                        }

                        @Override
                        public void onError(EncryptException encryptException) {
                            throw encryptException;
                        }
                    });

        // TODO: fix this somehow? countdown latch?
        Thread.sleep(2000);
    }

    @Test
    public void encrypt1() throws InterruptedException {
        cse.encrypt("123",
                    "123456",
                    new EncryptCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Assert.assertNotNull(result);
                        }

                        @Override
                        public void onError(EncryptException encryptException) {
                            throw encryptException;
                        }
                    });

        // TODO: fix this somehow? countdown latch?
        Thread.sleep(2000);
    }

    @Test
    public void encrypt2() throws InterruptedException {

        cse.encrypt("1",
                    "123456",
                    encryptCallback(
                            this::resultReceivedFailureExpected,
                            this.exceptionConsumer(EncryptExceptionCode.VALIDATION_FAILED)
                    )
        );

        // TODO: fix this somehow? countdown latch?
        Thread.sleep(2000);
    }


    @Test
    public void encrypt3() throws InterruptedException {
        cse.encrypt("4111 1111 1111 1111",
                    "John the Tester",
                    2018,
                    10,
                    "123",
                    "123456",
                    encryptCallback(
                            this::resultReceivedFailureExpected,
                            this.exceptionConsumer(EncryptExceptionCode.VALIDATION_FAILED, e -> {
                                Assert.assertTrue(cse.hasErrors());
                                Assert.assertEquals(1, cse.getErrors().size());
                                final String error = cse.getErrors().get(0);
                                Assert.assertEquals("EXPIRY_INVALID", error);
                            })
                    ));
        // TODO: fix this somehow? countdown latch?
        Thread.sleep(2000);
    }

    private void resultReceivedFailureExpected(String result) {
        Assert.fail("Received result, expected failure");
    }

    @SuppressWarnings("SameParameterValue")
    private Consumer<EncryptException> exceptionConsumer(EncryptExceptionCode expectedCode) {
        return exceptionConsumer(expectedCode, e -> {
        });
    }

    private Consumer<EncryptException> exceptionConsumer(EncryptExceptionCode expectedCode, Consumer<EncryptException> exceptionConsumer) {
        return e -> {
            Assert.assertNotNull(e);
            Assert.assertEquals(expectedCode, e.getCode());
            exceptionConsumer.accept(e);
        };
    }

    private EncryptCallback encryptCallback(final Consumer<String> resultConsumer,
                                            final Consumer<EncryptException> errorConsumer) {
        return new EncryptCallback() {
            @Override
            public void onSuccess(String result) {
                resultConsumer.accept(result);
            }

            @Override
            public void onError(EncryptException encryptException) {
                errorConsumer.accept(encryptException);
            }
        };
    }


}
