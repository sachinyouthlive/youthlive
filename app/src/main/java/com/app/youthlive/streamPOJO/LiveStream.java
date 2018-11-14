package com.app.youthlive.streamPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mukul on 28/12/17.
 */

public class LiveStream {

    @SerializedName("aspect_ratio_height")
    @Expose
    private Integer aspectRatioHeight = 1080;
    @SerializedName("aspect_ratio_width")
    @Expose
    private Integer aspectRatioWidth = 1920;
    @SerializedName("billing_mode")
    @Expose
    private String billingMode = "pay_as_you_go";
    @SerializedName("broadcast_location")
    @Expose
    private String broadcastLocation = "eu_belgium";
    @SerializedName("encoder")
    @Expose
    private String encoder = "wowza_gocoder";
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("transcoder_type")
    @Expose
    private String transcoderType = "transcoded";
    @SerializedName("closed_caption_type")
    @Expose
    private String closedCaptionType = "none";
    @SerializedName("delivery_method")
    @Expose
    private String deliveryMethod = "push";
    @SerializedName("delivery_protocols")
    @Expose
    private List<String> deliveryProtocols = null;
    @SerializedName("delivery_type")
    @Expose
    private String deliveryType = "single-bitrate";
    @SerializedName("disable_authentication")
    @Expose
    private Boolean disableAuthentication = true;
    @SerializedName("hosted_page")
    @Expose
    private Boolean hostedPage = false;
    @SerializedName("hosted_page_description")
    @Expose
    private String hostedPageDescription = "";
    @SerializedName("hosted_page_logo_image")
    @Expose
    private String hostedPageLogoImage = "";
    @SerializedName("hosted_page_sharing_icons")
    @Expose
    private Boolean hostedPageSharingIcons = false;
    @SerializedName("hosted_page_title")
    @Expose
    private String hostedPageTitle = "";
    @SerializedName("low_latency")
    @Expose
    private Boolean lowLatency = true;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("player_countdown")
    @Expose
    private Boolean playerCountdown = false;
    @SerializedName("player_countdown_at")
    @Expose
    private String playerCountdownAt;
    @SerializedName("player_logo_image")
    @Expose
    private String playerLogoImage = "";
    @SerializedName("player_logo_position")
    @Expose
    private String playerLogoPosition = "top-left";
    @SerializedName("player_responsive")
    @Expose
    private Boolean playerResponsive = true;
    @SerializedName("player_type")
    @Expose
    private String playerType = "original_html5";
    @SerializedName("player_video_poster_image")
    @Expose
    private String playerVideoPosterImage = "";
    @SerializedName("player_width")
    @Expose
    private Integer playerWidth = 640;
    @SerializedName("recording")
    @Expose
    private Boolean recording = true;
    @SerializedName("remove_hosted_page_logo_image")
    @Expose
    private Boolean removeHostedPageLogoImage = true;
    @SerializedName("remove_player_logo_image")
    @Expose
    private Boolean removePlayerLogoImage = true;
    @SerializedName("remove_player_video_poster_image")
    @Expose
    private Boolean removePlayerVideoPosterImage = true;
    @SerializedName("source_url")
    @Expose
    private String sourceUrl = "";
    @SerializedName("target_delivery_protocol")
    @Expose
    private String targetDeliveryProtocol = "hls-https";
    @SerializedName("use_stream_source")
    @Expose
    private Boolean useStreamSource = false;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("video_fallback")
    @Expose
    private Boolean videoFallback = true;

    public Integer getAspectRatioHeight() {
        return aspectRatioHeight;
    }

    public void setAspectRatioHeight(Integer aspectRatioHeight) {
        this.aspectRatioHeight = aspectRatioHeight;
    }

    public Integer getAspectRatioWidth() {
        return aspectRatioWidth;
    }

    public void setAspectRatioWidth(Integer aspectRatioWidth) {
        this.aspectRatioWidth = aspectRatioWidth;
    }

    public String getBillingMode() {
        return billingMode;
    }

    public void setBillingMode(String billingMode) {
        this.billingMode = billingMode;
    }

    public String getBroadcastLocation() {
        return broadcastLocation;
    }

    public void setBroadcastLocation(String broadcastLocation) {
        this.broadcastLocation = broadcastLocation;
    }

    public String getEncoder() {
        return encoder;
    }

    public void setEncoder(String encoder) {
        this.encoder = encoder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTranscoderType() {
        return transcoderType;
    }

    public void setTranscoderType(String transcoderType) {
        this.transcoderType = transcoderType;
    }

    public String getClosedCaptionType() {
        return closedCaptionType;
    }

    public void setClosedCaptionType(String closedCaptionType) {
        this.closedCaptionType = closedCaptionType;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public List<String> getDeliveryProtocols() {
        return deliveryProtocols;
    }

    public void setDeliveryProtocols(List<String> deliveryProtocols) {
        this.deliveryProtocols = deliveryProtocols;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Boolean getDisableAuthentication() {
        return disableAuthentication;
    }

    public void setDisableAuthentication(Boolean disableAuthentication) {
        this.disableAuthentication = disableAuthentication;
    }

    public Boolean getHostedPage() {
        return hostedPage;
    }

    public void setHostedPage(Boolean hostedPage) {
        this.hostedPage = hostedPage;
    }

    public String getHostedPageDescription() {
        return hostedPageDescription;
    }

    public void setHostedPageDescription(String hostedPageDescription) {
        this.hostedPageDescription = hostedPageDescription;
    }

    public String getHostedPageLogoImage() {
        return hostedPageLogoImage;
    }

    public void setHostedPageLogoImage(String hostedPageLogoImage) {
        this.hostedPageLogoImage = hostedPageLogoImage;
    }

    public Boolean getHostedPageSharingIcons() {
        return hostedPageSharingIcons;
    }

    public void setHostedPageSharingIcons(Boolean hostedPageSharingIcons) {
        this.hostedPageSharingIcons = hostedPageSharingIcons;
    }

    public String getHostedPageTitle() {
        return hostedPageTitle;
    }

    public void setHostedPageTitle(String hostedPageTitle) {
        this.hostedPageTitle = hostedPageTitle;
    }

    public Boolean getLowLatency() {
        return lowLatency;
    }

    public void setLowLatency(Boolean lowLatency) {
        this.lowLatency = lowLatency;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getPlayerCountdown() {
        return playerCountdown;
    }

    public void setPlayerCountdown(Boolean playerCountdown) {
        this.playerCountdown = playerCountdown;
    }

    public String getPlayerCountdownAt() {
        return playerCountdownAt;
    }

    public void setPlayerCountdownAt(String playerCountdownAt) {
        this.playerCountdownAt = playerCountdownAt;
    }

    public String getPlayerLogoImage() {
        return playerLogoImage;
    }

    public void setPlayerLogoImage(String playerLogoImage) {
        this.playerLogoImage = playerLogoImage;
    }

    public String getPlayerLogoPosition() {
        return playerLogoPosition;
    }

    public void setPlayerLogoPosition(String playerLogoPosition) {
        this.playerLogoPosition = playerLogoPosition;
    }

    public Boolean getPlayerResponsive() {
        return playerResponsive;
    }

    public void setPlayerResponsive(Boolean playerResponsive) {
        this.playerResponsive = playerResponsive;
    }

    public String getPlayerType() {
        return playerType;
    }

    public void setPlayerType(String playerType) {
        this.playerType = playerType;
    }

    public String getPlayerVideoPosterImage() {
        return playerVideoPosterImage;
    }

    public void setPlayerVideoPosterImage(String playerVideoPosterImage) {
        this.playerVideoPosterImage = playerVideoPosterImage;
    }

    public Integer getPlayerWidth() {
        return playerWidth;
    }

    public void setPlayerWidth(Integer playerWidth) {
        this.playerWidth = playerWidth;
    }

    public Boolean getRecording() {
        return recording;
    }

    public void setRecording(Boolean recording) {
        this.recording = recording;
    }

    public Boolean getRemoveHostedPageLogoImage() {
        return removeHostedPageLogoImage;
    }

    public void setRemoveHostedPageLogoImage(Boolean removeHostedPageLogoImage) {
        this.removeHostedPageLogoImage = removeHostedPageLogoImage;
    }

    public Boolean getRemovePlayerLogoImage() {
        return removePlayerLogoImage;
    }

    public void setRemovePlayerLogoImage(Boolean removePlayerLogoImage) {
        this.removePlayerLogoImage = removePlayerLogoImage;
    }

    public Boolean getRemovePlayerVideoPosterImage() {
        return removePlayerVideoPosterImage;
    }

    public void setRemovePlayerVideoPosterImage(Boolean removePlayerVideoPosterImage) {
        this.removePlayerVideoPosterImage = removePlayerVideoPosterImage;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getTargetDeliveryProtocol() {
        return targetDeliveryProtocol;
    }

    public void setTargetDeliveryProtocol(String targetDeliveryProtocol) {
        this.targetDeliveryProtocol = targetDeliveryProtocol;
    }

    public Boolean getUseStreamSource() {
        return useStreamSource;
    }

    public void setUseStreamSource(Boolean useStreamSource) {
        this.useStreamSource = useStreamSource;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getVideoFallback() {
        return videoFallback;
    }

    public void setVideoFallback(Boolean videoFallback) {
        this.videoFallback = videoFallback;
    }

}
