package com.appleisle.tincase.service.inf;

import com.appleisle.tincase.dto.request.JoinForm;
import com.appleisle.tincase.dto.response.UserSummary;

import java.util.List;

public interface IUserService {
    void join(JoinForm joinForm);

    List<UserSummary> getUserList();
}
