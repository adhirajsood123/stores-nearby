package com.task.phone.loyltytask;

/**
 * Created by adhiraj on 18/9/15.
 */
public interface IAsyncCallback {
    public void onSuccessResponse(String successResponse);

    public void onErrorResponse(String errorResponse );
}
