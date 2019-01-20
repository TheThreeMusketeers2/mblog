
package com.mtons.mblog.config;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty({"aliyun.oss.config.enabled"})
public class OssConfig {
    @Value("${aliyun.oss.config.bucketName}")
    private String bucketName;
    @Value("${aliyun.oss.config.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.config.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.oss.config.secretAccessKey}")
    private String secretAccessKey;
    @Value("${aliyun.oss.config.remoteEndpoint}")
    private String remoteEndpoint;

    @Bean
    OSSClient getOssClient() {
        return new OSSClient(this.endpoint, this.accessKeyId, this.secretAccessKey);
    }

    public OssConfig() {
    }

    public String getBucketName() {
        return this.bucketName;
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public String getAccessKeyId() {
        return this.accessKeyId;
    }

    public String getSecretAccessKey() {
        return this.secretAccessKey;
    }

    public String getRemoteEndpoint() {
        return this.remoteEndpoint;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }

    public void setRemoteEndpoint(String remoteEndpoint) {
        this.remoteEndpoint = remoteEndpoint;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof OssConfig)) {
            return false;
        } else {
            OssConfig other = (OssConfig)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label71: {
                    Object this$bucketName = this.getBucketName();
                    Object other$bucketName = other.getBucketName();
                    if (this$bucketName == null) {
                        if (other$bucketName == null) {
                            break label71;
                        }
                    } else if (this$bucketName.equals(other$bucketName)) {
                        break label71;
                    }

                    return false;
                }

                Object this$endpoint = this.getEndpoint();
                Object other$endpoint = other.getEndpoint();
                if (this$endpoint == null) {
                    if (other$endpoint != null) {
                        return false;
                    }
                } else if (!this$endpoint.equals(other$endpoint)) {
                    return false;
                }

                label57: {
                    Object this$accessKeyId = this.getAccessKeyId();
                    Object other$accessKeyId = other.getAccessKeyId();
                    if (this$accessKeyId == null) {
                        if (other$accessKeyId == null) {
                            break label57;
                        }
                    } else if (this$accessKeyId.equals(other$accessKeyId)) {
                        break label57;
                    }

                    return false;
                }

                Object this$secretAccessKey = this.getSecretAccessKey();
                Object other$secretAccessKey = other.getSecretAccessKey();
                if (this$secretAccessKey == null) {
                    if (other$secretAccessKey != null) {
                        return false;
                    }
                } else if (!this$secretAccessKey.equals(other$secretAccessKey)) {
                    return false;
                }

                Object this$remoteEndpoint = this.getRemoteEndpoint();
                Object other$remoteEndpoint = other.getRemoteEndpoint();
                if (this$remoteEndpoint == null) {
                    if (other$remoteEndpoint == null) {
                        return true;
                    }
                } else if (this$remoteEndpoint.equals(other$remoteEndpoint)) {
                    return true;
                }

                return false;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof OssConfig;
    }

    public int hashCode() {
        int result = 1;
        Object $bucketName = this.getBucketName();
        result = result * 59 + ($bucketName == null ? 43 : $bucketName.hashCode());
        Object $endpoint = this.getEndpoint();
        result = result * 59 + ($endpoint == null ? 43 : $endpoint.hashCode());
        Object $accessKeyId = this.getAccessKeyId();
        result = result * 59 + ($accessKeyId == null ? 43 : $accessKeyId.hashCode());
        Object $secretAccessKey = this.getSecretAccessKey();
        result = result * 59 + ($secretAccessKey == null ? 43 : $secretAccessKey.hashCode());
        Object $remoteEndpoint = this.getRemoteEndpoint();
        result = result * 59 + ($remoteEndpoint == null ? 43 : $remoteEndpoint.hashCode());
        return result;
    }

    public String toString() {
        return "OssConfig(bucketName=" + this.getBucketName() + ", endpoint=" + this.getEndpoint() + ", accessKeyId=" + this.getAccessKeyId() + ", secretAccessKey=" + this.getSecretAccessKey() + ", remoteEndpoint=" + this.getRemoteEndpoint() + ")";
    }
}
