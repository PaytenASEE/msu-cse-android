package com.payten.msu.cse;

import java.util.List;

/**
 * Created by jasmin.suljic@monri.com
 * MSU CSE
 */
interface EncryptRequest {

    boolean validate();

    List<String> errors();

    String plain();
}
