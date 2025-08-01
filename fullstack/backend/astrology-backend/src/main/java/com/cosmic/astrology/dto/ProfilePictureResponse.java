package com.cosmic.astrology.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Profile Picture Response DTO
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Profile picture upload response")
public class ProfilePictureResponse {
    
    @Schema(description = "Profile picture URL", example = "/profile-pictures/user123_1672531200000.jpg")
    private String profilePictureUrl;
    
    @Schema(description = "Original filename", example = "profile.jpg")
    private String filename;
    
    @Schema(description = "File size in bytes", example = "2048576")
    private Long fileSize;
    
    @Schema(description = "File type/MIME type", example = "image/jpeg")
    private String fileType;
    
    @Schema(description = "Image width in pixels", example = "300")
    private Integer width;
    
    @Schema(description = "Image height in pixels", example = "300")
    private Integer height;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Upload timestamp")
    private LocalDateTime uploadedAt;
    
    @Schema(description = "Upload status", example = "SUCCESS")
    private String uploadStatus; // SUCCESS, FAILED, PROCESSING
    
    @Schema(description = "File hash for deduplication")
    private String fileHash;
    
    @Schema(description = "Thumbnail URL", example = "/profile-pictures/thumbnails/user123_thumb.jpg")
    private String thumbnailUrl;
    
    @Schema(description = "CDN URL if using external storage")
    private String cdnUrl;
    
    // Constructors
    public ProfilePictureResponse() {
        this.uploadedAt = LocalDateTime.now();
        this.uploadStatus = "SUCCESS";
    }
    
    public ProfilePictureResponse(String profilePictureUrl, String filename, Long fileSize) {
        this();
        this.profilePictureUrl = profilePictureUrl;
        this.filename = filename;
        this.fileSize = fileSize;
    }
    
    // Getters and Setters
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
    
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    
    public Integer getWidth() { return width; }
    public void setWidth(Integer width) { this.width = width; }
    
    public Integer getHeight() { return height; }
    public void setHeight(Integer height) { this.height = height; }
    
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
    
    public String getUploadStatus() { return uploadStatus; }
    public void setUploadStatus(String uploadStatus) { this.uploadStatus = uploadStatus; }
    
    public String getFileHash() { return fileHash; }
    public void setFileHash(String fileHash) { this.fileHash = fileHash; }
    
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    
    public String getCdnUrl() { return cdnUrl; }
    public void setCdnUrl(String cdnUrl) { this.cdnUrl = cdnUrl; }
    
    // Utility Methods
    @JsonProperty("fileSizeFormatted")
    public String getFileSizeFormatted() {
        if (fileSize == null) return "Unknown";
        
        long bytes = fileSize;
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }
    
    @JsonProperty("aspectRatio")
    public String getAspectRatio() {
        if (width == null || height == null || height == 0) return "Unknown";
        
        double ratio = (double) width / height;
        if (Math.abs(ratio - 1.0) < 0.1) return "Square";
        if (ratio > 1.0) return "Landscape";
        return "Portrait";
    }
    
    @JsonProperty("isSquare")
    public boolean isSquare() {
        return width != null && height != null && Math.abs(width - height) <= 10;
    }
    
    @JsonProperty("uploadSuccess")
    public boolean isUploadSuccess() {
        return "SUCCESS".equals(uploadStatus);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProfilePictureResponse that = (ProfilePictureResponse) obj;
        return Objects.equals(profilePictureUrl, that.profilePictureUrl) &&
               Objects.equals(filename, that.filename) &&
               Objects.equals(fileHash, that.fileHash);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(profilePictureUrl, filename, fileHash);
    }
    
    @Override
    public String toString() {
        return String.format("ProfilePictureResponse{filename='%s', fileSize=%d, uploadStatus='%s'}", 
                           filename, fileSize, uploadStatus);
    }
}
