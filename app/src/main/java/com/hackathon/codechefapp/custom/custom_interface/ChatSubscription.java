package com.hackathon.codechefapp.custom.custom_interface;

import com.hosopy.actioncable.Subscription;
import com.hosopy.actioncable.annotation.Data;
import com.hosopy.actioncable.annotation.Perform;

/**
 * Created by SANDIP JANA on 23-09-2018.
 */
public interface ChatSubscription extends Subscription {
    /*
     * Equivalent:
     *   perform("join")
     */
    @Perform("join")
    void join();

    /*
     * Equivalent:
     *   perform("send_message", JsonObjectFactory.fromJson("{body: \"...\", private: true}"))
     */
    @Perform("send_message")
    void sendMessage(@Data("body") String body, @Data("private") boolean isPrivate);
}
