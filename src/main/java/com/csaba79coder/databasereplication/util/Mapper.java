package com.csaba79coder.databasereplication.util;

import com.csaba79coder.databasereplication.entity.Account;
import com.csaba79coder.databasereplication.model.AccountRequest;
import com.csaba79coder.databasereplication.model.AccountResponse;
import org.modelmapper.ModelMapper;

public class Mapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static AccountResponse mapAccountEntityToResponseModel(Account entity) {
        return modelMapper.map(entity, AccountResponse.class);
    }

    public static Account mapAccountRequestModelToEntity(AccountRequest request) {
        return modelMapper.map(request, Account.class);
    }

    private Mapper() {

    }
}
