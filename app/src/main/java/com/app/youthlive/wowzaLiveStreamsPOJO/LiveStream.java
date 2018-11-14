package com.app.youthlive.wowzaLiveStreamsPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by TBX on 1/13/2018.
 */

public class LiveStream {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("transcoder_type")
    @Expose
    private String transcoderType;
    @SerializedName("billing_mode")
    @Expose
    private String billingMode;
    @SerializedName("broadcast_location")
    @Expose
    private String broadcastLocation;
    @SerializedName("recording")
    @Expose
    private Boolean recording;
    @SerializedName("closed_caption_type")
    @Expose
    private String closedCaptionType;
    @SerializedName("low_latency")
    @Expose
    private Boolean lowLatency;
    @SerializedName("encoder")
    @Expose
    private String encoder;
    @SerializedName("delivery_method")
    @Expose
    private String deliveryMethod;
    @SerializedName("delivery_protocol")
    @Expose
    private String deliveryProtocol;
    @SerializedName("target_delivery_protocol")
    @Expose
    private String targetDeliveryProtocol;
    @SerializedName("use_stream_source")
    @Expose
    private Boolean useStreamSource;
    @SerializedName("aspect_ratio_width")
    @Expose
    private Integer aspectRatioWidth;
    @SerializedName("aspect_ratio_height")
    @Expose
    private Integer aspectRatioHeight;
    @SerializedName("connection_code")
    @Expose
    private String connectionCode;
    @SerializedName("connection_code_expires_at")
    @Expose
    private String connectionCodeExpiresAt;
    @SerializedName("delivery_protocols")
    @Expose
    private List<String> deliveryProtocols = null;
    @SerializedName("source_connection_information")
    @Expose
    private SourceConnectionInformation sourceConnectionInformation;
    @SerializedName("video_fallback")
    @Expose
    private Boolean videoFallback;
    @SerializedName("player_id")
    @Expose
    private String playerId;
    @SerializedName("player_type")
    @Expose
    private String playerType;
    @SerializedName("player_responsive")
    @Expose
    private Boolean playerResponsive;
    @SerializedName("player_countdown")
    @Expose
    private Boolean playerCountdown;
    @SerializedName("player_embed_code")
    @Expose
    private String playerEmbedCode;
    @SerializedName("player_hls_playback_url")
    @Expose
    private String playerHlsPlaybackUrl;
    @SerializedName("hosted_page")
    @Expose
    private Boolean hostedPage;
    @SerializedName("hosted_page_title")
    @Expose
    private String hostedPageTitle;
    @SerializedName("hosted_page_url")
    @Expose
    private String hostedPageUrl;
    @SerializedName("hosted_page_description")
    @Expose
    private String hostedPageDescription;
    @SerializedName("hosted_page_sharing_icons")
    @Expose
    private Boolean hostedPageSharingIcons;
    @SerializedName("stream_targets")
    @Expose
    private List<StreamTarget> streamTargets = null;
    @SerializedName("direct_playback_urls")
    @Expose
    private DirectPlaybackUrls directPlaybackUrls;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("links")
    @Expose
    private List<Link> links = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Boolean getRecording() {
        return recording;
    }

    public void setRecording(Boolean recording) {
        this.recording = recording;
    }

    public String getClosedCaptionType() {
        return closedCaptionType;
    }

    public void setClosedCaptionType(String closedCaptionType) {
        this.closedCaptionType = closedCaptionType;
    }

    public Boolean getLowLatency() {
        return lowLatency;
    }

    public void setLowLatency(Boolean lowLatency) {
        this.lowLatency = lowLatency;
    }

    public String getEncoder() {
        return encoder;
    }

    public void setEncoder(String encoder) {
        this.encoder = encoder;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getDeliveryProtocol() {
        return deliveryProtocol;
    }

    public void setDeliveryProtocol(String deliveryProtocol) {
        this.deliveryProtocol = deliveryProtocol;
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

    public Integer getAspectRatioWidth() {
        return aspectRatioWidth;
    }

    public void setAspectRatioWidth(Integer aspectRatioWidth) {
        this.aspectRatioWidth = aspectRatioWidth;
    }

    public Integer getAspectRatioHeight() {
        return aspectRatioHeight;
    }

    public void setAspectRatioHeight(Integer aspectRatioHeight) {
        this.aspectRatioHeight = aspectRatioHeight;
    }

    public String getConnectionCode() {
        return connectionCode;
    }

    public void setConnectionCode(String connectionCode) {
        this.connectionCode = connectionCode;
    }

    public String getConnectionCodeExpiresAt() {
        return connectionCodeExpiresAt;
    }

    public void setConnectionCodeExpiresAt(String connectionCodeExpiresAt) {
        this.connectionCodeExpiresAt = connectionCodeExpiresAt;
    }

    public List<String> getDeliveryProtocols() {
        return deliveryProtocols;
    }

    public void setDeliveryProtocols(List<String> deliveryProtocols) {
        this.deliveryProtocols = deliveryProtocols;
    }

    public SourceConnectionInformation getSourceConnectionInformation() {
        return sourceConnectionInformation;
    }

    public void setSourceConnectionInformation(SourceConnectionInformation sourceConnectionInformation) {
        this.sourceConnectionInformation = sourceConnectionInformation;
    }

    public Boolean getVideoFallback() {
        return videoFallback;
    }

    public void setVideoFallback(Boolean videoFallback) {
        this.videoFallback = videoFallback;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerType() {
        return playerType;
    }

    public void setPlayerType(String playerType) {
        this.playerType = playerType;
    }

    public Boolean getPlayerResponsive() {
        return playerResponsive;
    }

    public void setPlayerResponsive(Boolean playerResponsive) {
        this.playerResponsive = playerResponsive;
    }

    public Boolean getPlayerCountdown() {
        return playerCountdown;
    }

    public void setPlayerCountdown(Boolean playerCountdown) {
        this.playerCountdown = playerCountdown;
    }

    public String getPlayerEmbedCode() {
        return playerEmbedCode;
    }

    public void setPlayerEmbedCode(String playerEmbedCode) {
        this.playerEmbedCode = playerEmbedCode;
    }

    public String getPlayerHlsPlaybackUrl() {
        return playerHlsPlaybackUrl;
    }

    public void setPlayerHlsPlaybackUrl(String playerHlsPlaybackUrl) {
        this.playerHlsPlaybackUrl = playerHlsPlaybackUrl;
    }

    public Boolean getHostedPage() {
        return hostedPage;
    }

    public void setHostedPage(Boolean hostedPage) {
        this.hostedPage = hostedPage;
    }

    public String getHostedPageTitle() {
        return hostedPageTitle;
    }

    public void setHostedPageTitle(String hostedPageTitle) {
        this.hostedPageTitle = hostedPageTitle;
    }

    public String getHostedPageUrl() {
        return hostedPageUrl;
    }

    public void setHostedPageUrl(String hostedPageUrl) {
        this.hostedPageUrl = hostedPageUrl;
    }

    public String getHostedPageDescription() {
        return hostedPageDescription;
    }

    public void setHostedPageDescription(String hostedPageDescription) {
        this.hostedPageDescription = hostedPageDescription;
    }

    public Boolean getHostedPageSharingIcons() {
        return hostedPageSharingIcons;
    }

    public void setHostedPageSharingIcons(Boolean hostedPageSharingIcons) {
        this.hostedPageSharingIcons = hostedPageSharingIcons;
    }

    public List<StreamTarget> getStreamTargets() {
        return streamTargets;
    }

    public void setStreamTargets(List<StreamTarget> streamTargets) {
        this.streamTargets = streamTargets;
    }

    public DirectPlaybackUrls getDirectPlaybackUrls() {
        return directPlaybackUrls;
    }

    public void setDirectPlaybackUrls(DirectPlaybackUrls directPlaybackUrls) {
        this.directPlaybackUrls = directPlaybackUrls;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }


}
