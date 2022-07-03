import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;
import com.atguigu.vodservice.utils.AliVodSdkUtil;

import java.util.List;

/**
 * @author quan
 * @date 2021-08-03
 */
public class PlayTest {
    public static void main(String[] args) throws Exception {
        String id = "b527e01051ca4cfdb953f5228da41e45";
        DefaultAcsClient client = AliVodSdkUtil.initVodClient("LTAI5tPkEaHFP2UWkyZbJ7Wy", "rkK8p6IIcSKhhdVI1wevyZvB21SoUi");

        getAuthPlayUrl(id, client);
        // getPlayUrl(id, client);
        // uploadVideo();
        // createUploadVideoReq(client);
        // deleteVideo(client, id);
    }

    // 删除远程视频
    static void deleteVideo(DefaultAcsClient client, String id) throws ClientException {
        DeleteVideoRequest request = new DeleteVideoRequest();
        //支持传入多个视频ID，多个用逗号分隔
        // request.setVideoIds("VideoId1,VideoId2");
        request.setVideoIds(id);
        DeleteVideoResponse response = client.getAcsResponse(request);
    }

    // 上传本地视频
    static void uploadVideo() {
        String title = "4~Java简单数据类型";
        String filePath = "D:\\Users\\48536\\Videos\\a.mp4";
        //本地文件上传
        UploadVideoRequest request = new UploadVideoRequest("LTAI5tPkEaHFP2UWkyZbJ7Wy",
                "rkK8p6IIcSKhhdVI1wevyZvB21SoUi", title, filePath);
        /* 可指定分片上传时每个分片的大小，默认为1M字节 */
        request.setPartSize(1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1， (注：该配置会占用服务器CPU资源，需根据服务器情况指定） */
        request.setTaskNum(1);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }


    // 创建上传请求，返回临时的上传地址
    static void createUploadVideoReq(DefaultAcsClient client) throws ClientException {
        String title = "4~Java简单数据类型";
        String filePath = "D:/Users/48536/Videos/4~Java简单数据类型.mp4";

        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        request.setTitle(title);
        request.setFileName(filePath);

        CreateUploadVideoResponse response = client.getAcsResponse(request);
        System.out.print("VideoId = " + response.getVideoId() + "\n");
        System.out.print("UploadAddress = " + response.getUploadAddress() + "\n");
        System.out.print("UploadAuth = " + response.getUploadAuth() + "\n");
    }

    // 获取视频凭证
    static void getAuthPlayUrl(String id, DefaultAcsClient client) throws ClientException {
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response;

        request.setVideoId(id);

        response = client.getAcsResponse(request);
        System.out.println("playUrl: " + response.getPlayAuth());
    }

    // 获取视频播放地址
    static void getPlayUrl(String id, DefaultAcsClient client) throws ClientException {
        // 创建获取地址视频请求对象和响应对象
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response;

        // 向请求对象里设置视频id值
        request.setVideoId(id);

        // 调用方法
        response = client.getAcsResponse(request);

        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        // 播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.println("url: " + playInfo.getPlayURL());
        }
        // 视频信息
        System.out.println("title: " + response.getVideoBase().getTitle());
    }
}
