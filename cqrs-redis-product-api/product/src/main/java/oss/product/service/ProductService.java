package oss.product.service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import lombok.extern.slf4j.Slf4j;
import oss.core.exception.EventApplyException;
import oss.core.infra.InfraConstants;
import oss.product.config.RedisCmd;
import oss.product.exception.ProductNotFoundException;
import oss.product.infra.ProductEventHandler;
import oss.product.infra.ProductEventStoreRepository;
import oss.product.infra.read.OrderReadRepository;
import oss.product.model.Product;
import oss.product.model.command.ProductCommand;

/**
 * Created by jaceshim on 2017. 3. 3..
 */
@Service
@Slf4j
@Transactional
public class ProductService {

	@Autowired
	private ProductEventHandler productEventHandler;

	@Autowired
	private ProductEventStoreRepository productEventStoreRepository;
	
	@Autowired
	private OrderReadRepository orderReadRepository;
	
	@Autowired
	private RedisCmd redisCmd;

//	@Autowired
//	private S3Client s3Cli;
	
//	@Autowired
//	private MediaConvertClient mcli;
	
    @Value("${aws.s3.bucket}")
    private String bucket;

	/**
	 * 상품 등록
	 *
	 * @param productCreateCommand
	 * @return
	 */
	public Product createProduct(ProductCommand.CreateProduct productCreateCommand) {
		// DB sequence를 통해서 유일한 productId값 생성
		final Long productId = productEventStoreRepository.createProductId();
		final Product product = Product.create(productId, productCreateCommand);
		// 이벤트 저장
		productEventHandler.save(product);

		return product;
	}
	
	/**
	 * 상품 등록 REDIS큐 저장 
	 *
	 * @param productCreateCommand
	 * @return
	 */
	public int product2Redis(Map<String, MultipartFile> files) {
		CsvSchema bootstrap = CsvSchema.emptySchema().withHeader();
		CsvMapper csvMapper = new CsvMapper();
		MappingIterator<Map<?, ?>> mappingIterator = null;
		
		Iterator itr = files.values().iterator();
		MultipartFile file = (MultipartFile) itr.next();
		List ld = null;
		try { 
			mappingIterator = csvMapper.reader(ProductCommand.CreateProduct.class).with(bootstrap).readValues(file.getInputStream()); 
			ld = mappingIterator.readAll();
		} catch (IOException e) {
			log.warn("fail file parse D:{}",file.getOriginalFilename());
			return -1;
		}
		
		/** 예외처리 고려 해야 됨
		 *파일 입력 도중 끈킨다면 ? 1. 재입력 프로세스 정의 2. 실패 라인 로깅 3. 남은 데이터 저장
		 */
		ld.stream().forEach(data -> {
			long n_res = redisCmd.push(InfraConstants.CREATE_SCV_Q_PRODUCT, data);
			if(n_res < 0) log.warn("fail data line = {}", data);
		});

		return 0;
	}
	
	/**
	 * 파일 S3 저장 
	 *
	 * @param productCreateCommand
	 * @return
	 */
//	public int file2S3(Map<String, MultipartFile> files) {
//		Iterator itr = files.values().iterator();
//		MultipartFile file = (MultipartFile) itr.next();
//		PutObjectResponse rslt = null;
//		try { 
//			rslt = s3Cli.putObject(PutObjectRequest.builder().bucket(bucket).key(file.getOriginalFilename()).build(), RequestBody.fromBytes(file.getBytes()));
//		} catch (Exception e) {
//			log.warn("fail file send D:{}",file.getOriginalFilename());
//			return -1;
//		}
//		
//		log.debug("send sucess, S={}, M={}",rslt.sdkHttpResponse().statusCode(),rslt.sdkHttpResponse().statusText());
//		return 0;
//	}
	
//	public static String createMediaJob(MediaConvertClient mc, String mcRoleARN, String fileInput) {
//        String s3path = fileInput.substring(0, fileInput.lastIndexOf('/') + 1) + "javasdk/out/";
//        String fileOutput = s3path + "index";
//        String thumbsOutput = s3path + "thumbs/";
//        String mp4Output = s3path + "mp4/";
//
//        try {
//            // snippet-start:[mediaconvert.java.createjob.getendpointurl]
//            DescribeEndpointsResponse res = mc
//                    .describeEndpoints(DescribeEndpointsRequest.builder().maxResults(20).build());
//
//            if (res.endpoints().size() <= 0) {
//                System.out.println("Cannot find MediaConvert service endpoint URL!");
//                System.exit(1);
//            }
//            String endpointURL = res.endpoints().get(0).url();
//            System.out.println("MediaConvert service URL: " + endpointURL);
//            System.out.println("MediaConvert role ARN: " + mcRoleARN);
//            System.out.println("MediaConvert input file: " + fileInput);
//            System.out.println("MediaConvert output path: " + s3path);
//            // snippet-end:[mediaconvert.java.createjob.getendpointurl]
//
//            MediaConvertClient emc = MediaConvertClient.builder()
//                    .region(Region.AP_NORTHEAST_2)
//                    .endpointOverride(URI.create(endpointURL))
//                    .build();
//
//            // output group preset HLS low profile
//            Output hlsLow = createOutput("hls_low", "_low", "_$dt$", 750000, 7, 1920, 1080, 640);
//            // output group preset HLS media profile
//            Output hlsMedium = createOutput("hls_medium", "_medium", "_$dt$", 1200000, 7, 1920, 1080, 1280);
//            // output group preset HLS high profile
//            Output hlsHigh = createOutput("hls_high", "_high", "_$dt$", 3500000, 8, 1920, 1080, 1920);
//            // snippet-start:[mediaconvert.java.createjob.create_hls_output]
//            OutputGroup appleHLS = OutputGroup.builder().name("Apple HLS").customName("Example")
//                    .outputGroupSettings(OutputGroupSettings.builder().type(OutputGroupType.HLS_GROUP_SETTINGS)
//                            .hlsGroupSettings(HlsGroupSettings.builder()
//                                    .directoryStructure(HlsDirectoryStructure.SINGLE_DIRECTORY)
//                                    .manifestDurationFormat(HlsManifestDurationFormat.INTEGER)
//                                    .streamInfResolution(HlsStreamInfResolution.INCLUDE)
//                                    .clientCache(HlsClientCache.ENABLED)
//                                    .captionLanguageSetting(HlsCaptionLanguageSetting.OMIT)
//                                    .manifestCompression(HlsManifestCompression.NONE)
//                                    .codecSpecification(HlsCodecSpecification.RFC_4281)
//                                    .outputSelection(HlsOutputSelection.MANIFESTS_AND_SEGMENTS)
//                                    .programDateTime(HlsProgramDateTime.EXCLUDE).programDateTimePeriod(600)
//                                    .timedMetadataId3Frame(HlsTimedMetadataId3Frame.PRIV).timedMetadataId3Period(10)
//                                    .destination(fileOutput).segmentControl(HlsSegmentControl.SEGMENTED_FILES)
//                                    .minFinalSegmentLength((double) 0).segmentLength(4).minSegmentLength(0).build())
//                            .build())
//                    .outputs(hlsLow, hlsMedium, hlsHigh).build();
//            // snippet-end:[mediaconvert.java.createjob.create_hls_output]
//            // snippet-start:[mediaconvert.java.createjob.create_file_output]
//            OutputGroup fileMp4 = OutputGroup.builder().name("File Group").customName("mp4")
//                    .outputGroupSettings(OutputGroupSettings.builder().type(OutputGroupType.FILE_GROUP_SETTINGS)
//                            .fileGroupSettings(FileGroupSettings.builder().destination(mp4Output).build()).build())
//                    .outputs(Output.builder().extension("mp4")
//                            .containerSettings(ContainerSettings.builder().container(ContainerType.MP4).build())
//                            .videoDescription(VideoDescription.builder().width(1280).height(720)
//                                    .scalingBehavior(ScalingBehavior.DEFAULT).sharpness(50).antiAlias(AntiAlias.ENABLED)
//                                    .timecodeInsertion(VideoTimecodeInsertion.DISABLED)
//                                    .colorMetadata(ColorMetadata.INSERT).respondToAfd(RespondToAfd.NONE)
//                                    .afdSignaling(AfdSignaling.NONE).dropFrameTimecode(DropFrameTimecode.ENABLED)
//                                    .codecSettings(VideoCodecSettings.builder().codec(VideoCodec.H_264)
//                                            .h264Settings(H264Settings.builder()
//                                                    .rateControlMode(H264RateControlMode.QVBR)
//                                                    .parControl(H264ParControl.INITIALIZE_FROM_SOURCE)
//                                                    .qualityTuningLevel(H264QualityTuningLevel.SINGLE_PASS)
//                                                    .qvbrSettings(
//                                                            H264QvbrSettings.builder().qvbrQualityLevel(8).build())
//                                                    .codecLevel(H264CodecLevel.AUTO).codecProfile(H264CodecProfile.MAIN)
//                                                    .maxBitrate(2400000)
//                                                    .framerateControl(H264FramerateControl.INITIALIZE_FROM_SOURCE)
//                                                    .gopSize(2.0).gopSizeUnits(H264GopSizeUnits.SECONDS)
//                                                    .numberBFramesBetweenReferenceFrames(2).gopClosedCadence(1)
//                                                    .gopBReference(H264GopBReference.DISABLED)
//                                                    .slowPal(H264SlowPal.DISABLED).syntax(H264Syntax.DEFAULT)
//                                                    .numberReferenceFrames(3).dynamicSubGop(H264DynamicSubGop.STATIC)
//                                                    .fieldEncoding(H264FieldEncoding.PAFF)
//                                                    .sceneChangeDetect(H264SceneChangeDetect.ENABLED).minIInterval(0)
//                                                    .telecine(H264Telecine.NONE)
//                                                    .framerateConversionAlgorithm(
//                                                            H264FramerateConversionAlgorithm.DUPLICATE_DROP)
//                                                    .entropyEncoding(H264EntropyEncoding.CABAC).slices(1)
//                                                    .unregisteredSeiTimecode(H264UnregisteredSeiTimecode.DISABLED)
//                                                    .repeatPps(H264RepeatPps.DISABLED)
//                                                    .adaptiveQuantization(H264AdaptiveQuantization.HIGH)
//                                                    .spatialAdaptiveQuantization(
//                                                            H264SpatialAdaptiveQuantization.ENABLED)
//                                                    .temporalAdaptiveQuantization(
//                                                            H264TemporalAdaptiveQuantization.ENABLED)
//                                                    .flickerAdaptiveQuantization(
//                                                            H264FlickerAdaptiveQuantization.DISABLED)
//                                                    .softness(0).interlaceMode(H264InterlaceMode.PROGRESSIVE).build())
//                                            .build())
//                                    .build())
//                            .audioDescriptions(AudioDescription.builder()
//                                    .audioTypeControl(AudioTypeControl.FOLLOW_INPUT)
//                                    .languageCodeControl(AudioLanguageCodeControl.FOLLOW_INPUT)
//                                    .codecSettings(AudioCodecSettings.builder().codec(AudioCodec.AAC)
//                                            .aacSettings(AacSettings.builder().codecProfile(AacCodecProfile.LC)
//                                                    .rateControlMode(AacRateControlMode.CBR)
//                                                    .codingMode(AacCodingMode.CODING_MODE_2_0).sampleRate(44100)
//                                                    .bitrate(160000).rawFormat(AacRawFormat.NONE)
//                                                    .specification(AacSpecification.MPEG4)
//                                                    .audioDescriptionBroadcasterMix(
//                                                            AacAudioDescriptionBroadcasterMix.NORMAL)
//                                                    .build())
//                                            .build())
//                                    .build())
//                            .build())
//                    .build();
//            // snippet-end:[mediaconvert.java.createjob.create_file_output]
//            // snippet-start:[mediaconvert.java.createjob.create_thumbnail_output]
//            OutputGroup thumbs = OutputGroup.builder().name("File Group").customName("thumbs")
//                    .outputGroupSettings(OutputGroupSettings.builder().type(OutputGroupType.FILE_GROUP_SETTINGS)
//                            .fileGroupSettings(FileGroupSettings.builder().destination(thumbsOutput).build()).build())
//                    .outputs(Output.builder().extension("jpg")
//                            .containerSettings(ContainerSettings.builder().container(ContainerType.RAW).build())
//                            .videoDescription(VideoDescription.builder().scalingBehavior(ScalingBehavior.DEFAULT)
//                                    .sharpness(50).antiAlias(AntiAlias.ENABLED)
//                                    .timecodeInsertion(VideoTimecodeInsertion.DISABLED)
//                                    .colorMetadata(ColorMetadata.INSERT).dropFrameTimecode(DropFrameTimecode.ENABLED)
//                                    .codecSettings(VideoCodecSettings.builder().codec(VideoCodec.FRAME_CAPTURE)
//                                            .frameCaptureSettings(FrameCaptureSettings.builder().framerateNumerator(1)
//                                                    .framerateDenominator(1).maxCaptures(10000000).quality(80).build())
//                                            .build())
//                                    .build())
//                            .build())
//                    .build();
//            // snippet-end:[mediaconvert.java.createjob.create_thumbnail_output]
//            Map<String, AudioSelector> audioSelectors = new HashMap<String, AudioSelector>();
//            audioSelectors.put("Audio Selector 1",
//                    AudioSelector.builder().defaultSelection(AudioDefaultSelection.DEFAULT).offset(0).build());
//            JobSettings jobSettings = JobSettings.builder().inputs(Input.builder().audioSelectors(audioSelectors)
//                    .videoSelector(
//                            VideoSelector.builder().colorSpace(ColorSpace.FOLLOW).rotate(InputRotate.DEGREE_0).build())
//                    .filterEnable(InputFilterEnable.AUTO).filterStrength(0).deblockFilter(InputDeblockFilter.DISABLED)
//                    .denoiseFilter(InputDenoiseFilter.DISABLED).psiControl(InputPsiControl.USE_PSI)
//                    .timecodeSource(InputTimecodeSource.EMBEDDED).fileInput(fileInput).build())
//                    .outputGroups(appleHLS, thumbs, fileMp4).build();
//
//            CreateJobRequest createJobRequest = CreateJobRequest.builder().role(mcRoleARN).settings(jobSettings)
//                    .build();
//
//           CreateJobResponse createJobResponse = emc.createJob(createJobRequest);
//          return createJobResponse.job().id();
//
//        } catch (MediaConvertException e) {
//            System.out.println(e.toString());
//            System.exit(0);
//        }
//        return "";
//    }

    // snippet-start:[mediaconvert.java.createjob.create_output]
//    private final static Output createOutput(String customName,
//                                             String nameModifier,
//                                             String segmentModifier,
//                                             int qvbrMaxBitrate,
//                                             int qvbrQualityLevel,
//                                             int originWidth,
//                                             int originHeight,
//                                             int targetWidth) {
//
//        int targetHeight = Math.round(originHeight * targetWidth / originWidth)
//                - (Math.round(originHeight * targetWidth / originWidth) % 4);
//        Output output = null;
//        try {
//            output = Output.builder().nameModifier(nameModifier).outputSettings(OutputSettings.builder()
//                    .hlsSettings(HlsSettings.builder().segmentModifier(segmentModifier).audioGroupId("program_audio")
//                            .iFrameOnlyManifest(HlsIFrameOnlyManifest.EXCLUDE).build())
//                    .build())
//                    .containerSettings(ContainerSettings.builder().container(ContainerType.M3_U8)
//                            .m3u8Settings(M3u8Settings.builder().audioFramesPerPes(4)
//                                    .pcrControl(M3u8PcrControl.PCR_EVERY_PES_PACKET).pmtPid(480).privateMetadataPid(503)
//                                    .programNumber(1).patInterval(0).pmtInterval(0).scte35Source(M3u8Scte35Source.NONE)
//                                    .scte35Pid(500).nielsenId3(M3u8NielsenId3.NONE).timedMetadata(TimedMetadata.NONE)
//                                    .timedMetadataPid(502).videoPid(481)
//                                    .audioPids(482, 483, 484, 485, 486, 487, 488, 489, 490, 491, 492).build())
//                            .build())
//                    .videoDescription(
//                            VideoDescription.builder().width(targetWidth).height(targetHeight)
//                                    .scalingBehavior(ScalingBehavior.DEFAULT).sharpness(50).antiAlias(AntiAlias.ENABLED)
//                                    .timecodeInsertion(VideoTimecodeInsertion.DISABLED)
//                                    .colorMetadata(ColorMetadata.INSERT).respondToAfd(RespondToAfd.NONE)
//                                    .afdSignaling(AfdSignaling.NONE).dropFrameTimecode(DropFrameTimecode.ENABLED)
//                                    .codecSettings(VideoCodecSettings.builder().codec(VideoCodec.H_264)
//                                            .h264Settings(H264Settings.builder()
//                                                    .rateControlMode(H264RateControlMode.QVBR)
//                                                    .parControl(H264ParControl.INITIALIZE_FROM_SOURCE)
//                                                    .qualityTuningLevel(H264QualityTuningLevel.SINGLE_PASS)
//                                                    .qvbrSettings(H264QvbrSettings.builder()
//                                                            .qvbrQualityLevel(qvbrQualityLevel).build())
//                                                    .codecLevel(H264CodecLevel.AUTO)
//                                                    .codecProfile((targetHeight > 720 && targetWidth > 1280)
//                                                            ? H264CodecProfile.HIGH
//                                                            : H264CodecProfile.MAIN)
//                                                    .maxBitrate(qvbrMaxBitrate)
//                                                    .framerateControl(H264FramerateControl.INITIALIZE_FROM_SOURCE)
//                                                    .gopSize(2.0).gopSizeUnits(H264GopSizeUnits.SECONDS)
//                                                    .numberBFramesBetweenReferenceFrames(2).gopClosedCadence(1)
//                                                    .gopBReference(H264GopBReference.DISABLED)
//                                                    .slowPal(H264SlowPal.DISABLED).syntax(H264Syntax.DEFAULT)
//                                                    .numberReferenceFrames(3).dynamicSubGop(H264DynamicSubGop.STATIC)
//                                                    .fieldEncoding(H264FieldEncoding.PAFF)
//                                                    .sceneChangeDetect(H264SceneChangeDetect.ENABLED).minIInterval(0)
//                                                    .telecine(H264Telecine.NONE)
//                                                    .framerateConversionAlgorithm(
//                                                            H264FramerateConversionAlgorithm.DUPLICATE_DROP)
//                                                    .entropyEncoding(H264EntropyEncoding.CABAC).slices(1)
//                                                    .unregisteredSeiTimecode(H264UnregisteredSeiTimecode.DISABLED)
//                                                    .repeatPps(H264RepeatPps.DISABLED)
//                                                    .adaptiveQuantization(H264AdaptiveQuantization.HIGH)
//                                                    .spatialAdaptiveQuantization(
//                                                            H264SpatialAdaptiveQuantization.ENABLED)
//                                                    .temporalAdaptiveQuantization(
//                                                            H264TemporalAdaptiveQuantization.ENABLED)
//                                                    .flickerAdaptiveQuantization(
//                                                            H264FlickerAdaptiveQuantization.DISABLED)
//                                                    .softness(0).interlaceMode(H264InterlaceMode.PROGRESSIVE).build())
//                                            .build())
//                                    .build())
//                    .audioDescriptions(AudioDescription.builder().audioTypeControl(AudioTypeControl.FOLLOW_INPUT)
//                            .languageCodeControl(AudioLanguageCodeControl.FOLLOW_INPUT)
//                            .codecSettings(AudioCodecSettings.builder().codec(AudioCodec.AAC).aacSettings(AacSettings
//                                    .builder().codecProfile(AacCodecProfile.LC).rateControlMode(AacRateControlMode.CBR)
//                                    .codingMode(AacCodingMode.CODING_MODE_2_0).sampleRate(44100).bitrate(96000)
//                                    .rawFormat(AacRawFormat.NONE).specification(AacSpecification.MPEG4)
//                                    .audioDescriptionBroadcasterMix(AacAudioDescriptionBroadcasterMix.NORMAL).build())
//                                    .build())
//                            .build())
//                    .build();
//        } catch (MediaConvertException e) {
//            System.out.println(e.toString());
//            System.exit(0);
//        }
//        return output;
//    }
	
	/**
	 * 상품명 변경
	 *
	 * @param productId
	 * @param productChangeNameCommand
	 * @return
	 * @throws EventApplyException
	 */
	public Product changeName(Long productId, ProductCommand.ChangeName productChangeNameCommand) {
		final Product product = this.getProduct(productId);
		product.changeName(productChangeNameCommand);

		productEventHandler.save(product);

		return product;
	}

	/**
	 * 상품 가격 변경
	 * @param productChangePriceCommand
	 * @return
	 * @throws EventApplyException
	 */
	public Product changePrice(Long productId, ProductCommand.ChangePrice productChangePriceCommand) {
		final Product product = this.getProduct(productId);
		product.changePrice(productChangePriceCommand);

		productEventHandler.save(product);

		return product;
	}

	/**
	 * 상품 조회
	 *
	 * @param productId
	 * @return
	 */
	public Product getProduct(Long productId) {
		final Product product = productEventHandler.find(productId);
		if (product == null) {
			throw new ProductNotFoundException(productId + "is not exists.");
		}
		return product;
	}

	/**
	 * 상품 수량 증가 처리
	 *
	 * @param productId
	 * @param productIncreaseQuantityCommand
	 * @return
	 * @throws EventApplyException
	 */
	public Product increaseQuantity(Long productId, ProductCommand.IncreaseQuantity productIncreaseQuantityCommand) {
		final Product product = this.getProduct(productId);
		product.increaseQuantity(productIncreaseQuantityCommand);

		productEventHandler.save(product);

		return product;
	}

	/**
	 * 상품 수량 감소 처리
	 *
	 * @param productId
	 * @param productDecreaseQuantityCommand
	 * @return
	 * @throws EventApplyException
	 */
	public Product decreaseQuantity(Long productId, ProductCommand.DecreaseQuantity productDecreaseQuantityCommand) {
		final Product product = this.getProduct(productId);
		product.decreaseQuantity(productDecreaseQuantityCommand);

		productEventHandler.save(product);

		return product;
	}
	
	public List getProductList(List orderItems) {
		return orderReadRepository.findProuctInfo(orderItems);
	}
}
