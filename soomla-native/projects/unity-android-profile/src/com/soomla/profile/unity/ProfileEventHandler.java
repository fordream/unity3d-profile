package com.soomla.profile.unity;

import com.soomla.BusProvider;
import com.soomla.SoomlaUtils;
import com.soomla.profile.domain.IProvider;
import com.soomla.profile.domain.UserProfile;
import com.soomla.profile.events.ProfileInitializedEvent;
import com.soomla.profile.events.UserProfileUpdatedEvent;
import com.soomla.profile.events.UserRatingEvent;
import com.soomla.profile.events.auth.LoginCancelledEvent;
import com.soomla.profile.events.auth.LoginFailedEvent;
import com.soomla.profile.events.auth.LoginFinishedEvent;
import com.soomla.profile.events.auth.LoginStartedEvent;
import com.soomla.profile.events.auth.LogoutFailedEvent;
import com.soomla.profile.events.auth.LogoutFinishedEvent;
import com.soomla.profile.events.auth.LogoutStartedEvent;
import com.soomla.profile.events.social.*;
import com.soomla.profile.social.ISocialProvider;
import com.squareup.otto.Subscribe;
import com.unity3d.player.UnityPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileEventHandler {

    private static ProfileEventHandler mLocalEventHandler;
    private static String TAG = "SOOMLA Unity StoreEventHandler";

    public static void initialize() {
        SoomlaUtils.LogDebug("SOOMLA Unity ProfileEventHandler", "Initializing ProfileEventHandler ...");
        getInstance();
    }

    public static ProfileEventHandler getInstance() {
        if (mLocalEventHandler == null) {
            mLocalEventHandler = new ProfileEventHandler();
        }
        return mLocalEventHandler;
    }

    public ProfileEventHandler() {
        BusProvider.getInstance().register(this);
    }

    @Subscribe
    public void onProfileInitializedEvent(final ProfileInitializedEvent profileInitializedEvent){
        UnityPlayer.UnitySendMessage("ProfileEvents", "onSoomlaProfileInitialized", "");
    }

    @Subscribe
    public void onUserRatingEvent(final UserRatingEvent userRatingEvent){
        UnityPlayer.UnitySendMessage("ProfileEvents", "onUserRatingEvent", "");
    }

    @Subscribe
    public void onUserProfileUpdated(final UserProfileUpdatedEvent userProfileUpdatedEvent){
        UserProfile userProfile = userProfileUpdatedEvent.UserProfile;
        IProvider.Provider provider = userProfile.getProvider();
        JSONObject eventJSON = new JSONObject();
        try {
            eventJSON.put("userProfile", userProfile.toJSONObject());
            UnitySendFilteredMessage(eventJSON.toString(), "onUserProfileUpdated", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Subscribe
    public void onLoginStarted(final LoginStartedEvent loginStartedEvent){
        IProvider.Provider provider = loginStartedEvent.Provider;
        String payload = loginStartedEvent.Payload;
        JSONObject eventJSON = new JSONObject();
        try {
            eventJSON.put("provider", provider.getValue());
            eventJSON.put("autoLogin", loginStartedEvent.AutoLogin);
            eventJSON.put("payload", payload);
            UnitySendFilteredMessage(eventJSON.toString(), "onLoginStarted", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Subscribe
    public void onLoginFinished(final LoginFinishedEvent loginFinishedEvent){
        UserProfile userProfile = loginFinishedEvent.UserProfile;
        String payload = loginFinishedEvent.Payload;
        IProvider.Provider provider = userProfile.getProvider();
        JSONObject eventJSON = new JSONObject();
        try {
            eventJSON.put("userProfile", userProfile.toJSONObject());
            eventJSON.put("autoLogin", loginFinishedEvent.AutoLogin);
            eventJSON.put("payload", payload);
            UnitySendFilteredMessage(eventJSON.toString(), "onLoginFinished", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Subscribe
    public void onLoginCancelled(final LoginCancelledEvent loginCancelledEvent){
        IProvider.Provider provider = loginCancelledEvent.Provider;
        String payload = loginCancelledEvent.Payload;
        JSONObject eventJSON = new JSONObject();
        try {
            eventJSON.put("provider", provider.getValue());
            eventJSON.put("autoLogin", loginCancelledEvent.AutoLogin);
            eventJSON.put("payload", payload);
            UnitySendFilteredMessage(eventJSON.toString(), "onLoginCancelled", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Subscribe
    public void onLoginFailed(final LoginFailedEvent loginFailedEvent){
        IProvider.Provider provider = loginFailedEvent.Provider;
        String message = loginFailedEvent.ErrorDescription;
        String payload = loginFailedEvent.Payload;
        JSONObject eventJSON = new JSONObject();
        try {
            eventJSON.put("provider", provider.getValue());
            eventJSON.put("message", message);
            eventJSON.put("autoLogin", loginFailedEvent.AutoLogin);
            eventJSON.put("payload", payload);
            UnitySendFilteredMessage(eventJSON.toString(), "onLoginFailed", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Subscribe
    public void onLogoutStarted(final LogoutStartedEvent logoutStartedEvent){
        IProvider.Provider provider = logoutStartedEvent.Provider;
        JSONObject eventJSON = new JSONObject();
        try {
            eventJSON.put("provider", provider.getValue());
            UnitySendFilteredMessage(eventJSON.toString(), "onLogoutStarted", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Subscribe
    public void onLogoutFinished(final LogoutFinishedEvent logoutFinishedEvent){
        IProvider.Provider provider = logoutFinishedEvent.Provider;
        JSONObject eventJSON = new JSONObject();
        try {
            eventJSON.put("provider", provider.getValue());
            UnitySendFilteredMessage(eventJSON.toString(), "onLogoutFinished", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Subscribe
    public void onLogoutFailed(final LogoutFailedEvent logoutFailedEvent){
        IProvider.Provider provider = logoutFailedEvent.Provider;
        JSONObject eventJSON = new JSONObject();
        try {
            eventJSON.put("provider", provider.getValue());
            UnitySendFilteredMessage(eventJSON.toString(), "onLogoutFailed", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Subscribe
    public void onSocialActionStarted(final SocialActionStartedEvent socialActionStartedEvent){
        IProvider.Provider provider = socialActionStartedEvent.Provider;
        ISocialProvider.SocialActionType socialActionType = socialActionStartedEvent.SocialActionType;
        String payload = socialActionStartedEvent.Payload;
        JSONObject eventJSON = new JSONObject();
        try {
            eventJSON.put("provider", provider.getValue());
            eventJSON.put("socialActionType", socialActionType.getValue());
            eventJSON.put("payload", payload);
            UnitySendFilteredMessage(eventJSON.toString(), "onSocialActionStarted", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Subscribe
    public void onSocialActionFinished(final SocialActionFinishedEvent socialActionFinishedEvent){
        IProvider.Provider provider = socialActionFinishedEvent.Provider;
        ISocialProvider.SocialActionType socialActionType = socialActionFinishedEvent.SocialActionType;
        String payload = socialActionFinishedEvent.Payload;
        JSONObject eventJSON = new JSONObject();
        try {
            eventJSON.put("provider", provider.getValue());
            eventJSON.put("socialActionType", socialActionType.getValue());
            eventJSON.put("payload", payload);
            UnitySendFilteredMessage(eventJSON.toString(), "onSocialActionFinished", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Subscribe
    public void onSocialActionCancelled(final SocialActionCancelledEvent socialActionCancelledEvent){
        IProvider.Provider provider = socialActionCancelledEvent.Provider;
        ISocialProvider.SocialActionType socialActionType = socialActionCancelledEvent.SocialActionType;
        String payload = socialActionCancelledEvent.Payload;
        JSONObject eventJSON = new JSONObject();
        try {
            eventJSON.put("provider", provider.getValue());
            eventJSON.put("socialActionType", socialActionType.getValue());
            eventJSON.put("payload", payload);
            UnitySendFilteredMessage(eventJSON.toString(), "onSocialActionCancelled", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Subscribe
    public void onSocialActionFailed(final SocialActionFailedEvent socialActionFailedEvent){
        IProvider.Provider provider = socialActionFailedEvent.Provider;
        ISocialProvider.SocialActionType socialActionType = socialActionFailedEvent.SocialActionType;
        String message = socialActionFailedEvent.ErrorDescription;
        String payload = socialActionFailedEvent.Payload;
        JSONObject eventJSON = new JSONObject();
        try {
            eventJSON.put("provider", provider.getValue());
            eventJSON.put("socialActionType", socialActionType.getValue());
            eventJSON.put("message", message);
            eventJSON.put("payload", payload);
            UnitySendFilteredMessage(eventJSON.toString(), "onSocialActionFailed", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Subscribe
    public void onGetContactsStarted(final GetContactsStartedEvent getContactsStartedEvent){
        IProvider.Provider provider = getContactsStartedEvent.Provider;
        String payload = getContactsStartedEvent.Payload;
		boolean fromStart = getContactsStartedEvent.FromStart;
        JSONObject eventJSON = new JSONObject();
        try {
            eventJSON.put("provider", provider.getValue());
            eventJSON.put("payload", payload);
			eventJSON.put("fromStart", fromStart);
            UnitySendFilteredMessage(eventJSON.toString(), "onGetContactsStarted", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Subscribe
    public void onGetContactsFinished(final GetContactsFinishedEvent getContactsFinishedEvent){
        IProvider.Provider provider = getContactsFinishedEvent.Provider;
        String payload = getContactsFinishedEvent.Payload;
        boolean hasMore = getContactsFinishedEvent.HasMore;

        List<UserProfile> contacts = getContactsFinishedEvent.Contacts;
        try{
            JSONArray contactsJSONArray = new JSONArray();
            for (UserProfile contact : contacts) {
                contactsJSONArray.put(contact.toJSONObject());
            }

            JSONObject eventJSON = new JSONObject();
            eventJSON.put("provider", provider.getValue());
            eventJSON.put("contacts", contactsJSONArray);
            eventJSON.put("payload", payload);
            eventJSON.put("hasMore", hasMore);
            UnitySendFilteredMessage(eventJSON.toString(), "onGetContactsFinished", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Subscribe
    public void onGetContactsFailed(final GetContactsFailedEvent getContactsFailedEvent){
        IProvider.Provider provider = getContactsFailedEvent.Provider;
        String message = getContactsFailedEvent.ErrorDescription;
        String payload = getContactsFailedEvent.Payload;
        JSONObject eventJSON = new JSONObject();
        try {
            eventJSON.put("provider", provider.getValue());
            eventJSON.put("message", message);
            eventJSON.put("payload", payload);
            UnitySendFilteredMessage(eventJSON.toString(), "onGetContactsFailed", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Subscribe
    public void onGetFeedStarted(final GetFeedStartedEvent getFeedStartedEvent){
        IProvider.Provider provider = getFeedStartedEvent.Provider;
        String payload = getFeedStartedEvent.Payload;
        boolean fromStart = getFeedStartedEvent.FromStart;
        JSONObject eventJSON = new JSONObject();
        try {
            eventJSON.put("provider", provider.getValue());
            eventJSON.put("payload", payload);
            eventJSON.put("fromStart", fromStart);
            UnitySendFilteredMessage(eventJSON.toString(), "onGetFeedStarted", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Subscribe
    public void onGetFeedFinished(final GetFeedFinishedEvent getFeedFinishedEvent){
        IProvider.Provider provider = getFeedFinishedEvent.Provider;
        String payload = getFeedFinishedEvent.Payload;
        boolean hasMore = getFeedFinishedEvent.HasMore;
        List<String> feeds = getFeedFinishedEvent.Posts;
        try{
            JSONArray feedsJSONArray = new JSONArray();
            for (String feed: feeds) {
                feedsJSONArray.put(feed);
            }

            JSONObject eventJSON = new JSONObject();
            eventJSON.put("provider", provider.getValue());
            eventJSON.put("feeds", feedsJSONArray);
            eventJSON.put("payload", payload);
            eventJSON.put("hasMore", hasMore);

            UnitySendFilteredMessage(eventJSON.toString(), "onGetFeedFinished", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Subscribe
    public void onGetFeedFailed(final GetFeedFailedEvent getFeedFailedEvent){
        IProvider.Provider provider = getFeedFailedEvent.Provider;
        String message = getFeedFailedEvent.ErrorDescription;
        String payload = getFeedFailedEvent.Payload;
        JSONObject eventJSON = new JSONObject();
        try {
            eventJSON.put("provider", provider.getValue());
            eventJSON.put("message", message);
            eventJSON.put("payload", payload);
            UnitySendFilteredMessage(eventJSON.toString(), "onGetFeedFailed", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Subscribe
    public void onInviteStarted(final InviteStartedEvent inviteStartedEvent){
        IProvider.Provider provider = inviteStartedEvent.Provider;
        ISocialProvider.SocialActionType socialActionType = inviteStartedEvent.SocialActionType;
        String payload = inviteStartedEvent.Payload;
        JSONObject eventJSON = new JSONObject();
        try {
            eventJSON.put("provider", provider.getValue());
            eventJSON.put("socialActionType", socialActionType.getValue());
            eventJSON.put("payload", payload);
            UnitySendFilteredMessage(eventJSON.toString(), "onInviteStarted", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Subscribe
    public void onInviteFinished(final InviteFinishedEvent inviteFinishedEvent){
        IProvider.Provider provider = inviteFinishedEvent.Provider;
        ISocialProvider.SocialActionType socialActionType = inviteFinishedEvent.SocialActionType;
        String requestId = inviteFinishedEvent.RequestId;
        JSONArray invitedJson = new JSONArray(inviteFinishedEvent.InvitedIds);
        String payload = inviteFinishedEvent.Payload;
        JSONObject eventJSON = new JSONObject();
        try {
            eventJSON.put("provider", provider.getValue());
            eventJSON.put("socialActionType", socialActionType.getValue());
            eventJSON.put("requestId", requestId);
            eventJSON.put("invitedIds", invitedJson);
            eventJSON.put("payload", payload);
            UnitySendFilteredMessage(eventJSON.toString(), "onInviteFinished", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Subscribe
    public void onInviteCancelled(final InviteCancelledEvent inviteCancelledEvent){
        IProvider.Provider provider = inviteCancelledEvent.Provider;
        ISocialProvider.SocialActionType socialActionType = inviteCancelledEvent.SocialActionType;
        String payload = inviteCancelledEvent.Payload;
        JSONObject eventJSON = new JSONObject();
        try {
            eventJSON.put("provider", provider.getValue());
            eventJSON.put("socialActionType", socialActionType.getValue());
            eventJSON.put("payload", payload);
            UnitySendFilteredMessage(eventJSON.toString(), "onInviteCancelled", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Subscribe
    public void onInviteFailed(final InviteFailedEvent inviteFailedEvent){
        IProvider.Provider provider = inviteFailedEvent.Provider;
        ISocialProvider.SocialActionType socialActionType = inviteFailedEvent.SocialActionType;
        String message = inviteFailedEvent.ErrorDescription;
        String payload = inviteFailedEvent.Payload;
        JSONObject eventJSON = new JSONObject();
        try {
            eventJSON.put("provider", provider.getValue());
            eventJSON.put("socialActionType", socialActionType.getValue());
            eventJSON.put("message", message);
            eventJSON.put("payload", payload);
            UnitySendFilteredMessage(eventJSON.toString(), "onInviteFailed", provider.getValue());
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void UnitySendFilteredMessage(String message, String recipient, int provider) {
        //don't send to facebook!
        if (provider == 0)
        {
            SoomlaUtils.LogDebug(TAG, "Not sending event to provider: " + provider);
            return;
        }
        UnityPlayer.UnitySendMessage("ProfileEvents", recipient, message);
    }

    /**************************************************************************************************/
    // events pushed from external provider (Unity FB SDK etc.)

    public static void pushEventLoginStarted(String providerStr, boolean autoLogin, String payload) {
        IProvider.Provider provider = IProvider.Provider.getEnum(providerStr);
        BusProvider.getInstance().post(new LoginStartedEvent(provider, autoLogin, payload));
    }

    public static void pushEventLoginFinished(String userProfileJSON, boolean autoLogin, String payload) throws Exception {
        JSONObject jsonObject = new JSONObject(userProfileJSON);
        UserProfile userProfile = new UserProfile(jsonObject);
        BusProvider.getInstance().post(new LoginFinishedEvent(userProfile, autoLogin, payload));
    }

    public static void pushEventLoginFailed(String providerStr, String message, boolean autoLogin, String payload) {
        IProvider.Provider provider = IProvider.Provider.getEnum(providerStr);
        BusProvider.getInstance().post(new LoginFailedEvent(provider, message, autoLogin, payload));
    }

    public static void pushEventLoginCancelled(String providerStr, boolean autoLogin, String payload) {
        IProvider.Provider provider = IProvider.Provider.getEnum(providerStr);
        BusProvider.getInstance().post(new LoginCancelledEvent(provider, autoLogin, payload));
    }

    public static void pushEventLogoutStarted(String providerStr) {
        IProvider.Provider provider = IProvider.Provider.getEnum(providerStr);
        BusProvider.getInstance().post(new LogoutStartedEvent(provider));
    }

    public static void pushEventLogoutFinished(String providerStr) throws Exception {
        IProvider.Provider provider = IProvider.Provider.getEnum(providerStr);
        BusProvider.getInstance().post(new LogoutFinishedEvent(provider));
    }

    public static void pushEventLogoutFailed(String providerStr, String message) {
        IProvider.Provider provider = IProvider.Provider.getEnum(providerStr);
        BusProvider.getInstance().post(new LogoutFailedEvent(provider, message));
    }

    public static void pushEventSocialActionStarted(String providerStr, String actionTypeStr, String payload) {
        IProvider.Provider provider = IProvider.Provider.getEnum(providerStr);
        ISocialProvider.SocialActionType socialActionType = ISocialProvider.SocialActionType.getEnum(actionTypeStr);
        BusProvider.getInstance().post(new SocialActionStartedEvent(provider, socialActionType, payload));
    }

    public static void pushEventSocialActionFinished(String providerStr, String actionTypeStr, String payload) {
        IProvider.Provider provider = IProvider.Provider.getEnum(providerStr);
        ISocialProvider.SocialActionType socialActionType = ISocialProvider.SocialActionType.getEnum(actionTypeStr);
        BusProvider.getInstance().post(new SocialActionFinishedEvent(provider, socialActionType, payload));
    }

    public static void pushEventSocialActionCancelled(String providerStr, String actionTypeStr, String payload) {
        IProvider.Provider provider = IProvider.Provider.getEnum(providerStr);
        ISocialProvider.SocialActionType socialActionType = ISocialProvider.SocialActionType.getEnum(actionTypeStr);
        BusProvider.getInstance().post(new SocialActionCancelledEvent(provider, socialActionType, payload));
    }

    public static void pushEventSocialActionFailed(String providerStr, String actionTypeStr, String message, String payload) {
        IProvider.Provider provider = IProvider.Provider.getEnum(providerStr);
        ISocialProvider.SocialActionType socialActionType = ISocialProvider.SocialActionType.getEnum(actionTypeStr);
        BusProvider.getInstance().post(new SocialActionFailedEvent(provider, socialActionType, message, payload));
    }

    public static void pushEventGetContactsStarted(String providerStr, boolean fromStart, String payload) {
        IProvider.Provider provider = IProvider.Provider.getEnum(providerStr);
        BusProvider.getInstance().post(new GetContactsStartedEvent(provider, ISocialProvider.SocialActionType.GET_CONTACTS, fromStart, payload));
    }

    public static void pushEventGetContactsFinished(String providerStr, String userProfilesJSON, String payload, boolean hasMore) {
        IProvider.Provider provider = IProvider.Provider.getEnum(providerStr);
        List<UserProfile> contacts = new ArrayList<UserProfile> ();
        try {
            JSONArray jsonArray = new JSONArray(userProfilesJSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userProfileJSON = jsonArray.getJSONObject(i);
                UserProfile profile = new UserProfile(userProfileJSON);
                contacts.add(profile);
            }
        } catch (JSONException e) {
            SoomlaUtils.LogError(TAG, "(pushEventGetContactsFinished) Unable to parse user profiles from Unity " + userProfilesJSON +
                    "reason: " + e.getLocalizedMessage());
        }
        BusProvider.getInstance().post(new GetContactsFinishedEvent(provider, ISocialProvider.SocialActionType.GET_CONTACTS, contacts, payload, hasMore));
    }

    public static void pushEventGetContactsFailed(String providerStr, String message, Boolean fromStart, String payload) {
        IProvider.Provider provider = IProvider.Provider.getEnum(providerStr);
        BusProvider.getInstance().post(new GetContactsFailedEvent(provider, ISocialProvider.SocialActionType.GET_CONTACTS, message, fromStart, payload));
    }

    public static void pushEventInviteStarted(String providerStr, String actionTypeStr, String payload) {
        IProvider.Provider provider = IProvider.Provider.getEnum(providerStr);
        ISocialProvider.SocialActionType socialActionType = ISocialProvider.SocialActionType.getEnum(actionTypeStr);
        BusProvider.getInstance().post(new InviteStartedEvent(provider, socialActionType, payload));
    }

    public static void pushEventInviteFinished(String providerStr, String actionTypeStr, String requestId, String invitedIdsStr, String payload) {
        IProvider.Provider provider = IProvider.Provider.getEnum(providerStr);
        ISocialProvider.SocialActionType socialActionType = ISocialProvider.SocialActionType.getEnum(actionTypeStr);
        List<String> invitedIds = new ArrayList<String> ();
        try {
            JSONArray jsonInvited = new JSONArray(invitedIdsStr);
            for (int i = 0; i < jsonInvited.length(); i++) {
                invitedIds.add(jsonInvited.getString(i));
            }
        } catch (JSONException e) {
            SoomlaUtils.LogError(TAG, "(pushEventGetContactsFinished) Unable to parse user profiles from Unity " + invitedIdsStr +
                    "reason: " + e.getLocalizedMessage());
        }
        BusProvider.getInstance().post(new InviteFinishedEvent(provider, socialActionType, requestId, invitedIds, payload));
    }

    public static void pushEventInviteCancelled(String providerStr, String actionTypeStr, String payload) {
        IProvider.Provider provider = IProvider.Provider.getEnum(providerStr);
        ISocialProvider.SocialActionType socialActionType = ISocialProvider.SocialActionType.getEnum(actionTypeStr);
        BusProvider.getInstance().post(new InviteCancelledEvent(provider, socialActionType, payload));
    }

    public static void pushEventInviteFailed(String providerStr, String actionTypeStr, String message, String payload) {
        IProvider.Provider provider = IProvider.Provider.getEnum(providerStr);
        ISocialProvider.SocialActionType socialActionType = ISocialProvider.SocialActionType.getEnum(actionTypeStr);
        BusProvider.getInstance().post(new InviteFailedEvent(provider, socialActionType, message, payload));
    }
}
