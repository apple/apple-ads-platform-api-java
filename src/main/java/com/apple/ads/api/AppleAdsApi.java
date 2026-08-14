package com.apple.ads.api;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.MultipartBody;

import com.apple.ads.model.AdAccountCreate;
import com.apple.ads.model.AdAccountResponse;
import com.apple.ads.model.AdAccountUpdate;
import com.apple.ads.model.AdCreate;
import com.apple.ads.model.AdGroupCreate;
import com.apple.ads.model.AdGroupQueryResponse;
import com.apple.ads.model.AdGroupResponse;
import com.apple.ads.model.AdGroupUpdate;
import com.apple.ads.model.AdQueryResponse;
import com.apple.ads.model.AdResponse;
import com.apple.ads.model.AdUpdate;
import com.apple.ads.model.AdvertiserResourceListResponse;
import com.apple.ads.model.AdvertiserResourceType;
import com.apple.ads.model.AppDetailsResponse;
import com.apple.ads.model.AppLocaleDetailsQueryResponse;
import com.apple.ads.model.AppSupportedLanguagesQueryResponse;
import com.apple.ads.model.ApplyDailyCapRecommendation;
import com.apple.ads.model.ApplyTargetCpaRecommendation;
import com.apple.ads.model.AppsAdGroupReportResponse;
import com.apple.ads.model.AppsAdReportResponse;
import com.apple.ads.model.AppsCampaignReportResponse;
import com.apple.ads.model.AppsKeywordReportResponse;
import com.apple.ads.model.AppsReportingRequest;
import com.apple.ads.model.AppsSearchResponse;
import com.apple.ads.model.AppsSearchTermReportResponse;
import com.apple.ads.model.AssetQueryResponse;
import com.apple.ads.model.AssetResponse;
import com.apple.ads.model.AuditQuery;
import com.apple.ads.model.AuditSummaryResponse;
import com.apple.ads.model.BrandQueryResponse;
import com.apple.ads.model.BrandResponse;
import com.apple.ads.model.BrandsAdGroupReportResponse;
import com.apple.ads.model.BrandsAdReportResponse;
import com.apple.ads.model.BrandsCampaignReportResponse;
import com.apple.ads.model.BrandsKeywordReportResponse;
import com.apple.ads.model.BrandsReportingRequest;
import com.apple.ads.model.BrandsSearchTermReportResponse;
import com.apple.ads.model.BusinessCategoryQueryResponse;
import com.apple.ads.model.BusinessCategoryResponse;
import com.apple.ads.model.CampaignCreate;
import com.apple.ads.model.CampaignQueryResponse;
import com.apple.ads.model.CampaignResponse;
import com.apple.ads.model.CampaignUpdate;
import com.apple.ads.model.ChangeDetailsResponse;
import com.apple.ads.model.CreativeCreate;
import com.apple.ads.model.CreativeQueryResponse;
import com.apple.ads.model.CreativeRejectionReasonQueryRequest;
import com.apple.ads.model.CreativeRejectionReasonQueryResponse;
import com.apple.ads.model.CreativeResponse;
import com.apple.ads.model.CreativeUpdate;
import com.apple.ads.model.EligibilityQueryRequest;
import com.apple.ads.model.EligibilityQueryResponse;
import com.apple.ads.model.Error;
import com.apple.ads.model.ErrorResponse;
import java.io.File;
import com.apple.ads.model.GeoEntityType;
import com.apple.ads.model.GeoSearchPostRequest;
import com.apple.ads.model.GeoSearchResponse;
import com.apple.ads.model.ImpressionShareQueryRequest;
import com.apple.ads.model.ImpressionShareQueryResponse;
import com.apple.ads.model.KeywordCreate;
import com.apple.ads.model.KeywordCreateBulkRequest;
import com.apple.ads.model.KeywordCreateBulkResponse;
import com.apple.ads.model.KeywordQueryResponse;
import com.apple.ads.model.KeywordResponse;
import com.apple.ads.model.KeywordUpdate;
import com.apple.ads.model.KeywordUpdateBulkRequest;
import com.apple.ads.model.KeywordUpdateBulkResponse;
import com.apple.ads.model.LegacyAppLimitedStatusReasonDetailsResponse;
import com.apple.ads.model.LocationGroupCreate;
import com.apple.ads.model.LocationGroupQueryResponse;
import com.apple.ads.model.LocationGroupResponse;
import com.apple.ads.model.LocationGroupUpdate;
import com.apple.ads.model.LocationQueryResponse;
import com.apple.ads.model.LocationResponse;
import com.apple.ads.model.MeResponse;
import com.apple.ads.model.NegativeKeywordCreate;
import com.apple.ads.model.NegativeKeywordCreateBulkRequest;
import com.apple.ads.model.NegativeKeywordCreateBulkResponse;
import com.apple.ads.model.NegativeKeywordQueryResponse;
import com.apple.ads.model.NegativeKeywordResponse;
import com.apple.ads.model.NegativeKeywordUpdate;
import com.apple.ads.model.NegativeKeywordUpdateBulkRequest;
import com.apple.ads.model.NegativeKeywordUpdateBulkResponse;
import com.apple.ads.model.OrgResponse;
import com.apple.ads.model.PolicyAssignmentQueryRequest;
import com.apple.ads.model.PolicyAssignmentQueryResponse;
import com.apple.ads.model.ProductPageDetailsQueryResponse;
import com.apple.ads.model.ProductPageDetailsResponse;
import com.apple.ads.model.ProductPageLocaleDetailsQueryResponse;
import com.apple.ads.model.QueryRequest;
import com.apple.ads.model.RecommendationApplyDailyBudgetResponse;
import com.apple.ads.model.RecommendationApplyTargetCpaResponse;
import com.apple.ads.model.RecommendationDismissDailyBudgetResponse;
import com.apple.ads.model.RecommendationDismissTargetCpaResponse;
import com.apple.ads.model.RecommendationQueryCategorySuggestionResponse;
import com.apple.ads.model.RecommendationQueryDailyBudgetResponse;
import com.apple.ads.model.RecommendationQueryKeywordSuggestionResponse;
import com.apple.ads.model.RecommendationQueryPhraseSuggestionResponse;
import com.apple.ads.model.RecommendationQueryRequest;
import com.apple.ads.model.RecommendationQueryTargetCpaResponse;
import com.apple.ads.model.RecommendationQueryTargetCpaSuggestionResponse;
import com.apple.ads.model.RecommendationResponseBody;
import com.apple.ads.model.RejectionReasonResponse;
import com.apple.ads.model.Response;
import com.apple.ads.model.SearchSupplySourceType;
import com.apple.ads.model.SearchTermPopularityQueryRequest;
import com.apple.ads.model.SearchTermPopularityQueryResponse;
import com.apple.ads.model.SharedBudgetCreate;
import com.apple.ads.model.SharedBudgetQueryResponse;
import com.apple.ads.model.SharedBudgetResponse;
import com.apple.ads.model.SharedBudgetUpdate;
import java.util.UUID;
import com.apple.ads.model.UserAclListResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

public interface AppleAdsApi {
  /**
   * 
   * Get AdAccount by ID
   * @param id  (required)
   * @param xApContext  (required)
   * @return Call&lt;AdAccountResponse&gt;
   */
  @GET("ad-accounts/{id}")
  Call<AdAccountResponse> adAccountsIdGet(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext
  );

  /**
   * 
   * Update AdAccount
   * @param id  (required)
   * @param xApContext  (required)
   * @param adAccountUpdate  (optional)
   * @return Call&lt;AdAccountResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("ad-accounts/{id}")
  Call<AdAccountResponse> adAccountsIdPut(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body AdAccountUpdate adAccountUpdate
  );

  /**
   * 
   * Create AdAccount
   * @param adAccountCreate  (optional)
   * @return Call&lt;AdAccountResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("ad-accounts")
  Call<AdAccountResponse> adAccountsPost(
    @retrofit2.http.Body AdAccountCreate adAccountCreate
  );

  /**
   * 
   * Delete AdGroup by ID
   * @param id  (required)
   * @param xApContext  (required)
   * @return Call&lt;Response&gt;
   */
  @DELETE("adgroups/{id}")
  Call<Response> adgroupsIdDelete(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext
  );

  /**
   * 
   * Get AdGroup by ID
   * @param id  (required)
   * @param xApContext  (required)
   * @return Call&lt;AdGroupResponse&gt;
   */
  @GET("adgroups/{id}")
  Call<AdGroupResponse> adgroupsIdGet(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext
  );

  /**
   * 
   * Update AdGroup
   * @param id  (required)
   * @param xApContext  (required)
   * @param adGroupUpdate  (optional)
   * @return Call&lt;AdGroupResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("adgroups/{id}")
  Call<AdGroupResponse> adgroupsIdPut(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body AdGroupUpdate adGroupUpdate
  );

  /**
   * 
   * Create AdGroup
   * @param xApContext  (required)
   * @param adGroupCreate  (optional)
   * @return Call&lt;AdGroupResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("adgroups")
  Call<AdGroupResponse> adgroupsPost(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body AdGroupCreate adGroupCreate
  );

  /**
   * 
   * Query for AdGroup
   * @param xApContext  (required)
   * @param queryRequest  (optional)
   * @return Call&lt;AdGroupQueryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("adgroups/query")
  Call<AdGroupQueryResponse> adgroupsQueryPost(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body QueryRequest queryRequest
  );

  /**
   * 
   * Delete Ad by ID
   * @param id  (required)
   * @param xApContext  (required)
   * @return Call&lt;Response&gt;
   */
  @DELETE("ads/{id}")
  Call<Response> adsIdDelete(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext
  );

  /**
   * 
   * Get Ad by ID
   * @param id  (required)
   * @param xApContext  (required)
   * @return Call&lt;AdResponse&gt;
   */
  @GET("ads/{id}")
  Call<AdResponse> adsIdGet(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext
  );

  /**
   * 
   * Update Ad
   * @param id  (required)
   * @param xApContext  (required)
   * @param adUpdate  (optional)
   * @return Call&lt;AdResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("ads/{id}")
  Call<AdResponse> adsIdPut(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body AdUpdate adUpdate
  );

  /**
   * 
   * Create Ad
   * @param xApContext  (required)
   * @param adCreate  (optional)
   * @return Call&lt;AdResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("ads")
  Call<AdResponse> adsPost(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body AdCreate adCreate
  );

  /**
   * 
   * Query for Ad
   * @param xApContext  (required)
   * @param queryRequest  (optional)
   * @return Call&lt;AdQueryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("ads/query")
  Call<AdQueryResponse> adsQueryPost(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body QueryRequest queryRequest
  );

  /**
   * Apply daily budget recommendations
   * Apply one or more daily budget recommendations to campaigns.  **Requirements:** - All items must have the same &#x60;promotedObjectId&#x60; 
   * @param xApContext  (required)
   * @param applyDailyCapRecommendation  (required)
   * @return Call&lt;RecommendationApplyDailyBudgetResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("recommendations/daily-budgets/apply")
  Call<RecommendationApplyDailyBudgetResponse> applyDailyBudgetRecommendations(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body List<@Valid ApplyDailyCapRecommendation> applyDailyCapRecommendation
  );

  /**
   * Apply target CPA recommendations
   * Apply one or more target CPA recommendations to campaigns.  **Requirements:** - All items must have the same &#x60;promotedObjectId&#x60; 
   * @param xApContext  (required)
   * @param applyTargetCpaRecommendation  (required)
   * @return Call&lt;RecommendationApplyTargetCpaResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("recommendations/target-cpas/apply")
  Call<RecommendationApplyTargetCpaResponse> applyTargetCpaRecommendations(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body List<@Valid ApplyTargetCpaRecommendation> applyTargetCpaRecommendation
  );

  /**
   * Get app ad group reports
   * Retrieve reporting data for ad groups. **groupBy (APPS):** deviceClass, ageRange, gender, countryCode, adminArea, locality, storefront, countryOrRegion **HOURLY granularity** requires the date range to start within the last 7 days. **WEEKLY granularity** requires the end date to be at least 14 days in the past. **MONTHLY granularity** requires the end date to be at least 90 days in the past. 
   * @param xApContext  (required)
   * @param appsReportingRequest  (required)
   * @return Call&lt;AppsAdGroupReportResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("reports/apps/adgroups/query")
  Call<AppsAdGroupReportResponse> appsAdGroupReports(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body AppsReportingRequest appsReportingRequest
  );

  /**
   * Get app ad reports
   * Retrieve reporting data for ads. **groupBy (APPS):** deviceClass, countryOrRegion (ageRange, gender, countryCode, adminArea, locality, deviceClass excluded for AD) **HOURLY granularity** is not supported for the AD entity. **WEEKLY granularity** requires the end date to be at least 14 days in the past. **MONTHLY granularity** requires the end date to be at least 90 days in the past. 
   * @param xApContext  (required)
   * @param appsReportingRequest  (required)
   * @return Call&lt;AppsAdReportResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("reports/apps/ads/query")
  Call<AppsAdReportResponse> appsAdReports(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body AppsReportingRequest appsReportingRequest
  );

  /**
   * Get app campaign reports
   * Retrieve reporting data for campaigns. **groupBy (APPS):** deviceClass, ageRange, gender, countryCode, adminArea, locality, storefront, countryOrRegion **HOURLY granularity** requires the date range to start within the last 7 days. **WEEKLY granularity** requires the end date to be at least 14 days in the past. **MONTHLY granularity** requires the end date to be at least 90 days in the past. 
   * @param xApContext  (required)
   * @param appsReportingRequest  (required)
   * @return Call&lt;AppsCampaignReportResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("reports/apps/campaigns/query")
  Call<AppsCampaignReportResponse> appsCampaignReports(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body AppsReportingRequest appsReportingRequest
  );

  /**
   * Get app keyword reports
   * Retrieve reporting data for keywords. **groupBy (APPS):** deviceClass, countryOrRegion (ageRange, gender, countryCode, adminArea, locality excluded for KEYWORD) **Only ORTZ timezone** is supported for the KEYWORD entity when using SEARCHTERM reporting. Applies to both APPS and BRANDS. **HOURLY granularity** requires the date range to start within the last 7 days. **WEEKLY granularity** requires the end date to be at least 14 days in the past. **MONTHLY granularity** requires the end date to be at least 90 days in the past. 
   * @param xApContext  (required)
   * @param appsReportingRequest  (required)
   * @return Call&lt;AppsKeywordReportResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("reports/apps/keywords/query")
  Call<AppsKeywordReportResponse> appsKeywordReports(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body AppsReportingRequest appsReportingRequest
  );

  /**
   * Get app search term reports
   * Retrieve reporting data for search terms. **groupBy (APPS):** deviceClass, countryOrRegion (ageRange, gender, countryCode, adminArea, locality excluded for SEARCHTERM) **EMPTY_METRICS** is not supported for SEARCHTERM in either APPS or BRANDS. **Only ORTZ timezone** is supported for the SEARCHTERM entity. UTC is excluded for both APPS and BRANDS. **HOURLY granularity** is not supported for the SEARCHTERM entity. **WEEKLY granularity** requires the end date to be at least 14 days in the past. **MONTHLY granularity** requires the end date to be at least 90 days in the past. 
   * @param xApContext  (required)
   * @param appsReportingRequest  (required)
   * @return Call&lt;AppsSearchTermReportResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("reports/apps/searchterms/query")
  Call<AppsSearchTermReportResponse> appsSearchTermReports(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body AppsReportingRequest appsReportingRequest
  );

  /**
   * Get brand ad group reports
   * Retrieve reporting data for ad groups. **groupBy (BRANDS):** deviceClass, locationId, supplyPlacement **EMPTY_METRICS** is not supported for BRANDS. **HOURLY granularity** requires the date range to start within the last 7 days. **WEEKLY granularity** requires the end date to be at least 14 days in the past. **MONTHLY granularity** requires the end date to be at least 90 days in the past. 
   * @param xApContext  (required)
   * @param brandsReportingRequest  (required)
   * @return Call&lt;BrandsAdGroupReportResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("reports/business-brands/adgroups/query")
  Call<BrandsAdGroupReportResponse> brandsAdGroupReports(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body BrandsReportingRequest brandsReportingRequest
  );

  /**
   * Get brand ad reports
   * Retrieve reporting data for ads. **groupBy (BRANDS):** deviceClass, locationId, supplyPlacement **EMPTY_METRICS** is not supported for BRANDS. **HOURLY granularity** is not supported for the AD entity. **WEEKLY granularity** requires the end date to be at least 14 days in the past. **MONTHLY granularity** requires the end date to be at least 90 days in the past. 
   * @param xApContext  (required)
   * @param brandsReportingRequest  (required)
   * @return Call&lt;BrandsAdReportResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("reports/business-brands/ads/query")
  Call<BrandsAdReportResponse> brandsAdReports(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body BrandsReportingRequest brandsReportingRequest
  );

  /**
   * Get brand campaign reports
   * Retrieve reporting data for campaigns. **groupBy (BRANDS):** deviceClass, locationId, supplyPlacement **EMPTY_METRICS** is not supported for BRANDS. **HOURLY granularity** requires the date range to start within the last 7 days. **WEEKLY granularity** requires the end date to be at least 14 days in the past. **MONTHLY granularity** requires the end date to be at least 90 days in the past. 
   * @param xApContext  (required)
   * @param brandsReportingRequest  (required)
   * @return Call&lt;BrandsCampaignReportResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("reports/business-brands/campaigns/query")
  Call<BrandsCampaignReportResponse> brandsCampaignReports(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body BrandsReportingRequest brandsReportingRequest
  );

  /**
   * Get brand keyword reports
   * Retrieve reporting data for keywords. **groupBy (BRANDS):** deviceClass, locationId (supplyPlacement excluded for KEYWORD) **EMPTY_METRICS** is not supported for BRANDS. **Only ORTZ timezone** is supported for the KEYWORD entity when using SEARCHTERM reporting. Applies to both APPS and BRANDS. **HOURLY granularity** requires the date range to start within the last 7 days. **WEEKLY granularity** requires the end date to be at least 14 days in the past. **MONTHLY granularity** requires the end date to be at least 90 days in the past. 
   * @param xApContext  (required)
   * @param brandsReportingRequest  (required)
   * @return Call&lt;BrandsKeywordReportResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("reports/business-brands/keywords/query")
  Call<BrandsKeywordReportResponse> brandsKeywordReports(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body BrandsReportingRequest brandsReportingRequest
  );

  /**
   * Get brand search term reports
   * Retrieve reporting data for search terms. **groupBy (BRANDS):** deviceClass, locationId (supplyPlacement excluded for SEARCHTERM) **EMPTY_METRICS** is not supported for SEARCHTERM in either APPS or BRANDS. **Only ORTZ timezone** is supported for the SEARCHTERM entity. UTC is excluded for both APPS and BRANDS. **HOURLY granularity** is not supported for the SEARCHTERM entity. **WEEKLY granularity** requires the end date to be at least 14 days in the past. **MONTHLY granularity** requires the end date to be at least 90 days in the past. 
   * @param xApContext  (required)
   * @param brandsReportingRequest  (required)
   * @return Call&lt;BrandsSearchTermReportResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("reports/business-brands/searchterms/query")
  Call<BrandsSearchTermReportResponse> brandsSearchTermReports(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body BrandsReportingRequest brandsReportingRequest
  );

  /**
   * 
   * Delete Campaign by ID
   * @param id  (required)
   * @param xApContext  (required)
   * @return Call&lt;Response&gt;
   */
  @DELETE("campaigns/{id}")
  Call<Response> campaignsIdDelete(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext
  );

  /**
   * 
   * Get Campaign by ID
   * @param id  (required)
   * @param xApContext  (required)
   * @return Call&lt;CampaignResponse&gt;
   */
  @GET("campaigns/{id}")
  Call<CampaignResponse> campaignsIdGet(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext
  );

  /**
   * 
   * LegacyAppLimitedStatusReasonDetails for campaigns
   * @param id  (required)
   * @param xApContext  (required)
   * @return Call&lt;LegacyAppLimitedStatusReasonDetailsResponse&gt;
   */
  @GET("campaigns/{id}/legacy-app-limited-status-reason-details")
  Call<LegacyAppLimitedStatusReasonDetailsResponse> campaignsIdLegacyAppLimitedStatusReasonDetailsGet(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext
  );

  /**
   * 
   * Update Campaign
   * @param id  (required)
   * @param xApContext  (required)
   * @param campaignUpdate  (optional)
   * @return Call&lt;CampaignResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("campaigns/{id}")
  Call<CampaignResponse> campaignsIdPut(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body CampaignUpdate campaignUpdate
  );

  /**
   * 
   * Create Campaign
   * @param xApContext  (required)
   * @param campaignCreate  (optional)
   * @return Call&lt;CampaignResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("campaigns")
  Call<CampaignResponse> campaignsPost(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body CampaignCreate campaignCreate
  );

  /**
   * 
   * Query for Campaign
   * @param xApContext  (required)
   * @param queryRequest  (optional)
   * @return Call&lt;CampaignQueryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("campaigns/query")
  Call<CampaignQueryResponse> campaignsQueryPost(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body QueryRequest queryRequest
  );

  /**
   * Create Location Group
   * Creates a new location group
   * @param xApContext  (required)
   * @param locationGroupCreate Location group details to create (required)
   * @return Call&lt;LocationGroupResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("location-groups")
  Call<LocationGroupResponse> createLocationGroup(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body LocationGroupCreate locationGroupCreate
  );

  /**
   * 
   * Delete Creative by ID
   * @param id  (required)
   * @param xApContext  (required)
   * @return Call&lt;Response&gt;
   */
  @DELETE("creatives/{id}")
  Call<Response> creativesIdDelete(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext
  );

  /**
   * 
   * Get Creative by ID
   * @param id  (required)
   * @param xApContext  (required)
   * @return Call&lt;CreativeResponse&gt;
   */
  @GET("creatives/{id}")
  Call<CreativeResponse> creativesIdGet(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext
  );

  /**
   * 
   * Update Creative
   * @param id  (required)
   * @param xApContext  (required)
   * @param creativeUpdate  (optional)
   * @return Call&lt;CreativeResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("creatives/{id}")
  Call<CreativeResponse> creativesIdPut(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body CreativeUpdate creativeUpdate
  );

  /**
   * 
   * Create Creative
   * @param xApContext  (required)
   * @param creativeCreate  (optional)
   * @return Call&lt;CreativeResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("creatives")
  Call<CreativeResponse> creativesPost(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body CreativeCreate creativeCreate
  );

  /**
   * 
   * Query for Creative
   * @param xApContext  (required)
   * @param queryRequest  (optional)
   * @return Call&lt;CreativeQueryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("creatives/query")
  Call<CreativeQueryResponse> creativesQueryPost(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body QueryRequest queryRequest
  );

  /**
   * Delete Asset by ID
   * Deletes an asset by its unique identifier. 
   * @param xApContext  (required)
   * @param id Unique identifier of the asset to delete (required)
   * @return Call&lt;Response&gt;
   */
  @DELETE("assets/{id}")
  Call<Response> deleteAsset(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Path("id") UUID id
  );

  /**
   * Delete Location Group
   * Deletes a location group (soft delete)
   * @param xApContext  (required)
   * @param id Business Connect entity identifier (required)
   * @return Call&lt;LocationGroupResponse&gt;
   */
  @DELETE("location-groups/{id}")
  Call<LocationGroupResponse> deleteLocationGroup(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Path("id") String id
  );

  /**
   * Dismiss daily budget recommendations
   * Dismiss one or more daily budget recommendations.  **Requirements:** - All items must have the same &#x60;promotedObjectId&#x60; 
   * @param xApContext  (required)
   * @param applyDailyCapRecommendation  (required)
   * @return Call&lt;RecommendationDismissDailyBudgetResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("recommendations/daily-budgets/dismiss")
  Call<RecommendationDismissDailyBudgetResponse> dismissDailyBudgetRecommendations(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body List<@Valid ApplyDailyCapRecommendation> applyDailyCapRecommendation
  );

  /**
   * Dismiss target CPA recommendations
   * Dismiss one or more target CPA recommendations.  **Requirements:** - All items must have the same &#x60;promotedObjectId&#x60; 
   * @param xApContext  (required)
   * @param applyTargetCpaRecommendation  (required)
   * @return Call&lt;RecommendationDismissTargetCpaResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("recommendations/target-cpas/dismiss")
  Call<RecommendationDismissTargetCpaResponse> dismissTargetCpaRecommendations(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body List<@Valid ApplyTargetCpaRecommendation> applyTargetCpaRecommendation
  );

  /**
   * Find Apps eligibilities
   * Find Apps eligibilities
   * @param xApContext  (required)
   * @param eligibilityQueryRequest  (optional)
   * @return Call&lt;EligibilityQueryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("eligibilities/apps/query")
  Call<EligibilityQueryResponse> eligibilitiesAppsQueryPost(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body EligibilityQueryRequest eligibilityQueryRequest
  );

  /**
   * Get advertiser resources
   * Retrieves advertiser resources available to the authenticated API user, filtered by resource type. 
   * @param resourceType Filter by resource type. (required)
   * @return Call&lt;AdvertiserResourceListResponse&gt;
   */
  @GET("advertiser-resources")
  Call<AdvertiserResourceListResponse> getAdvertiserResources(
    @retrofit2.http.Query("resourceType") AdvertiserResourceType resourceType
  );

  /**
   * Get App Details by Adam ID
   * Retrieves application details for a specific Adam ID. 
   * @param xApContext  (required)
   * @param adamId App Store Connect Adam ID (required)
   * @return Call&lt;AppDetailsResponse&gt;
   */
  @GET("apps/{adamId}")
  Call<AppDetailsResponse> getAppDetailsByAdamId(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Path("adamId") Long adamId
  );

  /**
   * Get Asset by ID
   * Retrieves a single asset by its unique identifier.
   * @param xApContext  (required)
   * @param id Unique identifier of the asset (required)
   * @return Call&lt;AssetResponse&gt;
   */
  @GET("assets/{id}")
  Call<AssetResponse> getAsset(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Path("id") UUID id
  );

  /**
   * Get Brand by ID
   * Retrieves a single brand by its Business Connect ID
   * @param xApContext  (required)
   * @param id Business Connect entity identifier (required)
   * @return Call&lt;BrandResponse&gt;
   */
  @GET("business-brands/{id}")
  Call<BrandResponse> getBrand(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Path("id") String id
  );

  /**
   * Get Category by ID
   * Retrieves a single category by its Business Connect ID
   * @param xApContext  (required)
   * @param id Business Connect entity identifier (required)
   * @return Call&lt;BusinessCategoryResponse&gt;
   */
  @GET("business-categories/{id}")
  Call<BusinessCategoryResponse> getCategory(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Path("id") String id
  );

  /**
   * Get change details by detailId
   * Returns field-level change details for a single entity change. The detailId format is &#x60;EntityType.entityId.txnId&#x60; (e.g. &#x60;Campaign.123.abc-def&#x60;). 
   * @param xApContext  (required)
   * @param detailId Composite ID in format: EntityType.entityId.txnId (required)
   * @param limit  (optional, default to 100)
   * @param offset  (optional, default to 0)
   * @return Call&lt;ChangeDetailsResponse&gt;
   */
  @GET("change-history/{detailId}")
  Call<ChangeDetailsResponse> getChangeDetails(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Path("detailId") String detailId, @retrofit2.http.Query("limit") Integer limit, @retrofit2.http.Query("offset") Integer offset
  );

  /**
   * Gets a list of geo locations
   * Gets a list of geo locations for the specified list of geo id and entity type. Results are sorted alphabetically by display name, deduplicated, and paginated. Pagination parameters are in the request body (not query params).
   * @param xApContext  (required)
   * @param geoSearchPostRequest Geo search request with entity criteria and pagination. (required)
   * @return Call&lt;GeoSearchResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("search/geo")
  Call<GeoSearchResponse> getGeosByIds(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body GeoSearchPostRequest geoSearchPostRequest
  );

  /**
   * Get Location by ID
   * Retrieves a single location by its Business Connect ID
   * @param xApContext  (required)
   * @param id Business Connect entity identifier (required)
   * @return Call&lt;LocationResponse&gt;
   */
  @GET("locations/{id}")
  Call<LocationResponse> getLocation(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Path("id") String id
  );

  /**
   * Get Location Group by ID
   * Retrieves a single location group by its Business Connect ID
   * @param xApContext  (required)
   * @param id Business Connect entity identifier (required)
   * @return Call&lt;LocationGroupResponse&gt;
   */
  @GET("location-groups/{id}")
  Call<LocationGroupResponse> getLocationGroup(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Path("id") String id
  );

  /**
   * Get current user details
   * Returns the userId and orgId of the authenticated API caller. 
   * @return Call&lt;MeResponse&gt;
   */
  @GET("me")
  Call<MeResponse> getMe();
    

  /**
   * Get Product Page by ID
   * Retrieves a specific Product Page (DPP, CPP, or PPO) by its ID
   * @param xApContext  (required)
   * @param productPageId Product Page ID (required)
   * @return Call&lt;ProductPageDetailsResponse&gt;
   */
  @GET("product-pages/{productPageId}")
  Call<ProductPageDetailsResponse> getProductPageById(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Path("productPageId") String productPageId
  );

  /**
   * Get user ACLs
   * Retrieves the Access Control List (ACL) for the authenticated user, including their roles and associated ad accounts. 
   * @return Call&lt;UserAclListResponse&gt;
   */
  @GET("acls")
  Call<UserAclListResponse> getUserAcls();
    

  /**
   * Query app impression share data
   * Retrieve impression share data for promoted apps. Returns paginated JSON results directly (synchronous). Supports DAILY and WEEKLY granularity and two report types via options.impressionShareReportType: FIRST_SLOT (first ad position only) and ALL_SLOTS (all ad positions aggregated).  A filter on **promotedObjectId** is mandatory. **Timezone** is fixed to UTC. Maximum **pageSize** is 5000. Maximum **2 sort fields**. 
   * @param xApContext  (required)
   * @param impressionShareQueryRequest  (required)
   * @return Call&lt;ImpressionShareQueryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("insights/apps/impression-share/query")
  Call<ImpressionShareQueryResponse> impressionShareQuery(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body ImpressionShareQueryRequest impressionShareQueryRequest
  );

  /**
   * 
   * BulkCreate for keywords
   * @param xApContext  (required)
   * @param keywordCreateBulkRequest  (optional)
   * @return Call&lt;KeywordCreateBulkResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("keywords/bulk-create")
  Call<KeywordCreateBulkResponse> keywordsBulkCreatePost(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body KeywordCreateBulkRequest keywordCreateBulkRequest
  );

  /**
   * 
   * BulkUpdate for keywords
   * @param xApContext  (required)
   * @param keywordUpdateBulkRequest  (optional)
   * @return Call&lt;KeywordUpdateBulkResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("keywords/bulk-update")
  Call<KeywordUpdateBulkResponse> keywordsBulkUpdatePost(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body KeywordUpdateBulkRequest keywordUpdateBulkRequest
  );

  /**
   * 
   * Delete Keyword by ID
   * @param id  (required)
   * @param xApContext  (required)
   * @return Call&lt;Response&gt;
   */
  @DELETE("keywords/{id}")
  Call<Response> keywordsIdDelete(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext
  );

  /**
   * 
   * Get Keyword by ID
   * @param id  (required)
   * @param xApContext  (required)
   * @return Call&lt;KeywordResponse&gt;
   */
  @GET("keywords/{id}")
  Call<KeywordResponse> keywordsIdGet(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext
  );

  /**
   * 
   * Update Keyword
   * @param id  (required)
   * @param xApContext  (required)
   * @param keywordUpdate  (optional)
   * @return Call&lt;KeywordResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("keywords/{id}")
  Call<KeywordResponse> keywordsIdPut(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body KeywordUpdate keywordUpdate
  );

  /**
   * 
   * Create Keyword
   * @param xApContext  (required)
   * @param keywordCreate  (optional)
   * @return Call&lt;KeywordResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("keywords")
  Call<KeywordResponse> keywordsPost(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body KeywordCreate keywordCreate
  );

  /**
   * 
   * Query for Keyword
   * @param xApContext  (required)
   * @param queryRequest  (optional)
   * @return Call&lt;KeywordQueryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("keywords/query")
  Call<KeywordQueryResponse> keywordsQueryPost(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body QueryRequest queryRequest
  );

  /**
   * 
   * BulkCreate for negative-keywords
   * @param xApContext  (required)
   * @param negativeKeywordCreateBulkRequest  (optional)
   * @return Call&lt;NegativeKeywordCreateBulkResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("negative-keywords/bulk-create")
  Call<NegativeKeywordCreateBulkResponse> negativeKeywordsBulkCreatePost(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body NegativeKeywordCreateBulkRequest negativeKeywordCreateBulkRequest
  );

  /**
   * 
   * BulkUpdate for negative-keywords
   * @param xApContext  (required)
   * @param negativeKeywordUpdateBulkRequest  (optional)
   * @return Call&lt;NegativeKeywordUpdateBulkResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("negative-keywords/bulk-update")
  Call<NegativeKeywordUpdateBulkResponse> negativeKeywordsBulkUpdatePost(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body NegativeKeywordUpdateBulkRequest negativeKeywordUpdateBulkRequest
  );

  /**
   * 
   * Delete NegativeKeyword by ID
   * @param id  (required)
   * @param xApContext  (required)
   * @return Call&lt;Response&gt;
   */
  @DELETE("negative-keywords/{id}")
  Call<Response> negativeKeywordsIdDelete(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext
  );

  /**
   * 
   * Get NegativeKeyword by ID
   * @param id  (required)
   * @param xApContext  (required)
   * @return Call&lt;NegativeKeywordResponse&gt;
   */
  @GET("negative-keywords/{id}")
  Call<NegativeKeywordResponse> negativeKeywordsIdGet(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext
  );

  /**
   * 
   * Update NegativeKeyword
   * @param id  (required)
   * @param xApContext  (required)
   * @param negativeKeywordUpdate  (optional)
   * @return Call&lt;NegativeKeywordResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("negative-keywords/{id}")
  Call<NegativeKeywordResponse> negativeKeywordsIdPut(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body NegativeKeywordUpdate negativeKeywordUpdate
  );

  /**
   * 
   * Create NegativeKeyword
   * @param xApContext  (required)
   * @param negativeKeywordCreate  (optional)
   * @return Call&lt;NegativeKeywordResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("negative-keywords")
  Call<NegativeKeywordResponse> negativeKeywordsPost(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body NegativeKeywordCreate negativeKeywordCreate
  );

  /**
   * 
   * Query for NegativeKeyword
   * @param xApContext  (required)
   * @param queryRequest  (optional)
   * @return Call&lt;NegativeKeywordQueryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("negative-keywords/query")
  Call<NegativeKeywordQueryResponse> negativeKeywordsQueryPost(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body QueryRequest queryRequest
  );

  /**
   * 
   * Get Org by ID
   * @param id  (required)
   * @return Call&lt;OrgResponse&gt;
   */
  @GET("orgs/{id}")
  Call<OrgResponse> orgsIdGet(
    @retrofit2.http.Path("id") String id
  );

  /**
   * Query Default Product Page Locale Details by Adam ID
   * Retrieves localized content for the Default Product Page of a specific app.  Common filters include productPageId, language, languageCode, etc. 
   * @param xApContext  (required)
   * @param adamId App Store Connect Adam ID (required)
   * @param queryRequest Optional query criteria (filters, pagination, sort).  (optional)
   * @return Call&lt;AppLocaleDetailsQueryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("apps/{adamId}/locale-details/query")
  Call<AppLocaleDetailsQueryResponse> queryAppLocaleDetails(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Path("adamId") Long adamId, @retrofit2.http.Body QueryRequest queryRequest
  );

  /**
   * Query Assets
   * Retrieves a list of assets based on filtering, sorting, and pagination criteria.  Supports: - **Field selection**: Return only specified fields to reduce response size - **Complex filtering**: Multiple operators (EQUALS, NOT_EQUALS, IN, GREATER_THAN, etc.) - **Multi-field sorting**: Sort by multiple fields (ASC/DESC) - **Pagination**: Offset-based pagination with configurable page size  An empty request body returns all assets with default pagination (pageSize: 20). 
   * @param xApContext  (required)
   * @param queryRequest Query criteria for filtering, sorting, and paginating assets (optional)
   * @return Call&lt;AssetQueryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("assets/query")
  Call<AssetQueryResponse> queryAssets(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body QueryRequest queryRequest
  );

  /**
   * Query audit summary — grouped by transaction
   * Returns audit summaries grouped by (userType, userId, txnId, eventType, entityType). Requires at least an eventTime filter. Use options.needTotals&#x3D;false to skip the COUNT query. 
   * @param xApContext  (required)
   * @param auditQuery  (required)
   * @return Call&lt;AuditSummaryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("change-history/query")
  Call<AuditSummaryResponse> queryAuditSummary(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body AuditQuery auditQuery
  );

  /**
   * Query Brands
   * Retrieves a list of brands based on filtering, sorting, and pagination criteria. Using &#x60;\&quot;fields\&quot;: [\&quot;providerObjectId\&quot;]&#x60; returns a validation error. Filtering on &#x60;id&#x60; matches the Business Connect provider object ID. 
   * @param xApContext  (required)
   * @param queryRequest A query object to filter, sort, and paginate the results (required)
   * @return Call&lt;BrandQueryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("business-brands/query")
  Call<BrandQueryResponse> queryBrands(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body QueryRequest queryRequest
  );

  /**
   * Query Categories
   * Retrieves a list of categories based on filtering, sorting, and pagination criteria. Using &#x60;\&quot;fields\&quot;: [\&quot;providerObjectId\&quot;]&#x60; returns a validation error. 
   * @param xApContext  (required)
   * @param queryRequest A query object to filter, sort, and paginate the results (required)
   * @return Call&lt;BusinessCategoryQueryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("business-categories/query")
  Call<BusinessCategoryQueryResponse> queryCategories(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body QueryRequest queryRequest
  );

  /**
   * Query category suggestions
   * Query category suggestions. Supports two query routes based on &#x60;queryType&#x60;:          **Route 1: SUGGESTION** (discover categories for an app)          - &#x60;queryType&#x60; &#x3D; &#x60;SUGGESTION&#x60; (REQUIRED)          - &#x60;promotedObjectId&#x60; (REQUIRED)          - &#x60;promotedObjectType&#x60; (REQUIRED)          Returns suggested categories based on the promoted object.          **Route 2: SEARCH** (look up or search categories)          - &#x60;queryType&#x60; &#x3D; &#x60;SEARCH&#x60; (REQUIRED)          - &#x60;category&#x60; (REQUIRED) with operator:            - &#x60;IN&#x60;: fetches popularity for each provided category            - &#x60;LIKE&#x60;: performs a string match search across categories          
   * @param xApContext  (required)
   * @param recommendationQueryRequest  (required)
   * @return Call&lt;RecommendationQueryCategorySuggestionResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("suggestions/categories/query")
  Call<RecommendationQueryCategorySuggestionResponse> queryCategorySuggestions(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body RecommendationQueryRequest recommendationQueryRequest
  );

  /**
   * Query daily budget recommendations
   * Retrieve daily budget recommendations for campaigns.           **Required Filters:**          - &#x60;promotedObjectId&#x60; (campaign ID)          - &#x60;promotedObjectType&#x60; (type of promoted object)           
   * @param xApContext  (required)
   * @param recommendationQueryRequest  (required)
   * @return Call&lt;RecommendationQueryDailyBudgetResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("recommendations/daily-budgets/query")
  Call<RecommendationQueryDailyBudgetResponse> queryDailyBudgetRecommendations(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body RecommendationQueryRequest recommendationQueryRequest
  );

  /**
   * Query keyword suggestions
   * Query keyword suggestions based on search terms and storefronts using structured filters.           **Required Filters:**          - &#x60;promotedObjectId&#x60; (campaign or app ID)          - &#x60;promotedObjectType&#x60; (type of promoted object)           **Optional Filters:**          - &#x60;terms&#x60; (search terms to get suggestions for)          - &#x60;countriesOrRegions&#x60; (storefront codes to target)          
   * @param xApContext  (required)
   * @param recommendationQueryRequest  (required)
   * @return Call&lt;RecommendationQueryKeywordSuggestionResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("suggestions/keywords/query")
  Call<RecommendationQueryKeywordSuggestionResponse> queryKeywordSuggestions(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body RecommendationQueryRequest recommendationQueryRequest
  );

  /**
   * Query Location Groups
   * Retrieves a list of location groups based on filtering, sorting, and pagination criteria. Using &#x60;\&quot;fields\&quot;: [\&quot;providerObjectId\&quot;]&#x60; returns a validation error. Filtering on &#x60;id&#x60; matches the Business Connect provider object ID.  ### Supported Filter Fields | Field | Type | Operators | Notes | |-------|------|-----------|-------| | &#x60;id&#x60; | string | EQUALS, IN | Maps to provider object ID | | &#x60;name&#x60; | string | EQUALS, CONTAINS | Group name | | &#x60;brandId&#x60; | string | EQUALS | Parent brand | | &#x60;businessOrgId&#x60; | string | EQUALS | Parent org | | &#x60;groupType&#x60; | string (enum) | EQUALS, IN | STATIC or DYNAMIC | | &#x60;deleted&#x60; | boolean | EQUALS | Soft-delete flag | | &#x60;isAllLocationsGroup&#x60; | boolean | EQUALS | All-locations flag | | &#x60;eligibility.status&#x60; | string (enum) | EQUALS, IN | Eligibility status | | &#x60;eligibility.blockedGroups.supplyPlacement&#x60; | string | CONTAINS_ANY | Blocked placement | | &#x60;eligibility.blockedGroups.countryOrRegion&#x60; | string | CONTAINS_ANY | Blocked country | | &#x60;eligibility.allowedGroups.supplyPlacement&#x60; | string | CONTAINS_ANY | Allowed placement | | &#x60;eligibility.allowedGroups.countryOrRegion&#x60; | string | CONTAINS_ANY | Allowed country | 
   * @param xApContext  (required)
   * @param queryRequest A query object to filter, sort, and paginate the results (required)
   * @return Call&lt;LocationGroupQueryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("location-groups/query")
  Call<LocationGroupQueryResponse> queryLocationGroups(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body QueryRequest queryRequest
  );

  /**
   * Query Locations
   * Retrieves a list of locations based on filtering, sorting, and pagination criteria. Using &#x60;\&quot;fields\&quot;: [\&quot;providerObjectId\&quot;]&#x60; returns a validation error. 
   * @param xApContext  (required)
   * @param queryRequest A query object to filter, sort, and paginate the results (required)
   * @return Call&lt;LocationQueryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("locations/query")
  Call<LocationQueryResponse> queryLocations(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body QueryRequest queryRequest
  );

  /**
   * Query phrase suggestions
   * Query phrase suggestions. Supports two query routes based on &#x60;queryType&#x60;:          **Route 1: SUGGESTION** (discover phrases for an app)          - &#x60;queryType&#x60; &#x3D; &#x60;SUGGESTION&#x60; (REQUIRED)          - &#x60;promotedObjectId&#x60; (REQUIRED)          - &#x60;promotedObjectType&#x60; (REQUIRED)          Returns suggested phrases based on the promoted object.          **Route 2: SEARCH** (look up or search phrases)          - &#x60;queryType&#x60; &#x3D; &#x60;SEARCH&#x60; (REQUIRED)          - &#x60;phrase&#x60; (REQUIRED) with operator:            - &#x60;IN&#x60;: fetches popularity for each provided phrase            - &#x60;LIKE&#x60;: performs a string match search across phrases          
   * @param xApContext  (required)
   * @param recommendationQueryRequest  (required)
   * @return Call&lt;RecommendationQueryPhraseSuggestionResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("suggestions/phrases/query")
  Call<RecommendationQueryPhraseSuggestionResponse> queryPhraseSuggestions(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body RecommendationQueryRequest recommendationQueryRequest
  );

  /**
   * Query Product Page Locale Details
   * Retrieves localized content for Product Pages including app names, descriptions, and assets by device.  Common filters include productPageId, language, languageCode, etc. 
   * @param xApContext  (required)
   * @param queryRequest Query criteria for product page locale details (optional)
   * @return Call&lt;ProductPageLocaleDetailsQueryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("product-pages/locale-details/query")
  Call<ProductPageLocaleDetailsQueryResponse> queryProductPageLocaleDetails(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body QueryRequest queryRequest
  );

  /**
   * Query Product Pages
   * Retrieves a list of Product Pages (DPP, CPPs, PPOs) based on filtering, sorting, and pagination criteria.  Common filters include adamId, state, etc. 
   * @param xApContext  (required)
   * @param queryRequest Query criteria for product pages (optional)
   * @return Call&lt;ProductPageDetailsQueryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("product-pages/query")
  Call<ProductPageDetailsQueryResponse> queryProductPages(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body QueryRequest queryRequest
  );

  /**
   * Query policy assignments (rejection reasons) for external consumers
   * Returns paginated policy assignment records enriched with rejection reason details. 
   * @param xApContext  (required)
   * @param policyAssignmentQueryRequest  (optional)
   * @return Call&lt;PolicyAssignmentQueryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("rejection-reasons/business-brands/query")
  Call<PolicyAssignmentQueryResponse> queryRejectionReasonsByBusinessBrand(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body PolicyAssignmentQueryRequest policyAssignmentQueryRequest
  );

  /**
   * Query Supported App Languages
   * Retrieves metadata about supported languages for apps, including language codes and locale information.  Common filters include countryCode, name, etc. 
   * @param xApContext  (required)
   * @param queryRequest Query criteria for supported app languages (optional)
   * @return Call&lt;AppSupportedLanguagesQueryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("metadata/apps/supported-languages/query")
  Call<AppSupportedLanguagesQueryResponse> querySupportedAppLanguages(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body QueryRequest queryRequest
  );

  /**
   * Query target CPA recommendations
   * Retrieve target cost-per-acquisition recommendations for campaigns.           **Required Filters:**          - &#x60;promotedObjectId&#x60; (campaign ID)          - &#x60;promotedObjectType&#x60; (type of promoted object)           
   * @param xApContext  (required)
   * @param recommendationQueryRequest  (required)
   * @return Call&lt;RecommendationQueryTargetCpaResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("recommendations/target-cpas/query")
  Call<RecommendationQueryTargetCpaResponse> queryTargetCpaRecommendations(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body RecommendationQueryRequest recommendationQueryRequest
  );

  /**
   * Query target CPA suggestion
   * Retrieve the suggested Target CPA bid for a new Max Conversions campaign on the App Store. The suggestion is the maximum tap-install CPI across the requested countries or regions that have at least 10 installs in the last 28 days.           **Required Filters:**          - &#x60;promotedObjectId&#x60; (single numeric app ID, &#x60;EQUALS&#x60;)          - &#x60;promotedObjectType&#x60; (&#x60;EQUALS&#x60; &#x60;APPSTORE_APP&#x60;)           **Optional Filters:**          - &#x60;countryOrRegion&#x60; (one or more ISO country/region codes). When omitted, all countries or regions with eligible install volume are considered.           Rate limited to 5 QPS per (user, organization).          
   * @param xApContext  (required)
   * @param recommendationQueryRequest  (required)
   * @return Call&lt;RecommendationQueryTargetCpaSuggestionResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("suggestions/target-cpas/query")
  Call<RecommendationQueryTargetCpaSuggestionResponse> queryTargetCpaSuggestion(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body RecommendationQueryRequest recommendationQueryRequest
  );

  /**
   * Find rejection reasons
   * Find Apps rejection reasons using a query
   * @param xApContext  (required)
   * @param creativeRejectionReasonQueryRequest  (optional)
   * @return Call&lt;CreativeRejectionReasonQueryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("rejection-reasons/apps/query")
  Call<CreativeRejectionReasonQueryResponse> rejectionReasonsAppsQueryPost(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body CreativeRejectionReasonQueryRequest creativeRejectionReasonQueryRequest
  );

  /**
   * Gets rejection reasons by id
   * Gets Apps rejection reasons
   * @param xApContext  (required)
   * @param rejectionReasonId  (required)
   * @return Call&lt;RejectionReasonResponse&gt;
   */
  @GET("rejection-reasons/apps/{rejectionReasonId}")
  Call<RejectionReasonResponse> rejectionReasonsAppsRejectionReasonIdGet(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Path("rejectionReasonId") Long rejectionReasonId
  );

  /**
   * Searches for a list of apps
   * Searches the App Store for apps matching the supplied criteria.  **Behavior:** - At least one of &#x60;query&#x60;, &#x60;cpids&#x60;, or &#x60;returnOwnedApps&#x3D;true&#x60; must be supplied.   Calls with none of these are rejected with &#x60;INVALID_INPUT&#x60;. - When &#x60;query&#x60; is supplied it must contain at least one alphanumeric   character; minimum length is 3 characters (2 for CJK languages). - &#x60;storeFronts&#x60; may be repeated to filter by multiple country or region codes.   All codes must be enabled storefronts. - Results are paginated; &#x60;limit&#x60; is capped at the service-side maximum   (&#x60;MAXLIMIT&#x60;) regardless of the requested value. - Response items include enrichment such as &#x60;appIcon&#x60;, &#x60;contentProviderId&#x60;,   &#x60;softwareRating&#x60;, &#x60;displayPolicies&#x60; and &#x60;eligibilities&#x60;. 
   * @param xApContext  (required)
   * @param query Free-text app search criteria. Matched against app name and developer name. Must contain at least one alphanumeric character. Minimum length of 3 characters (2 for CJK languages).  (optional)
   * @param returnOwnedApps When &#x60;true&#x60;, returns apps owned by the caller&#39;s organization. May be combined with &#x60;query&#x60; and &#x60;cpids&#x60;, or used on its own when neither is supplied.  (optional, default to false)
   * @param cpids Comma-separated list of iTunes content provider identifiers used to scope the search to apps owned by those providers.  (optional)
   * @param storeFronts One or more storefront country or region codes (ISO 3166-1 alpha-2) used to filter results. Repeat the parameter to supply multiple values. All values must be enabled storefronts; otherwise the request is rejected with &#x60;INVALID_COUNTRY_CODE&#x60;.  (optional)
   * @param offset Offset of the first page result to return. (optional, default to 0)
   * @param limit Maximum number of entries to return on a page. Capped at the service-side maximum (&#x60;MAXLIMIT&#x60;).  (optional, default to 20)
   * @return Call&lt;AppsSearchResponse&gt;
   */
  @GET("search/apps")
  Call<AppsSearchResponse> searchApps(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Query("query") String query, @retrofit2.http.Query("returnOwnedApps") Boolean returnOwnedApps, @retrofit2.http.Query("cpids") String cpids, @retrofit2.http.Query("storeFronts") List<String> storeFronts, @retrofit2.http.Query("offset") Integer offset, @retrofit2.http.Query("limit") Integer limit
  );

  /**
   * Searches for a list of geo locations
   * Searches for a list of geo locations based on specified criteria.  **Behavior:** - &#x60;supplySource&#x60; is **required** — specifies the supply source context (APPSTORE or MAPS) - Hard-blocked geos (NO_MUID, NOT_SUPPORTED, SOURCE_REMOVED, COUNTRY_NOT_SUPPORTED,   COUNTRY_NOT_SEARCHABLE) are always excluded from results - Soft-blocked geos (LOCALITY_LOW_SEARCH_VOLUME, POSTAL_CODE_SPARSE) pass through   with eligibility data by default — the UI decides how to present them - With &#x60;eligible&#x3D;true&#x60;: soft-blocked geos are also excluded  **Supply source scoping:** - &#x60;supplySource&#x3D;MAPS&#x60;: Countries excluded, results restricted to US/CA - &#x60;supplySource&#x3D;APPSTORE&#x60;: PostalCode excluded  **Query constraints:** - Minimum query length: 2 characters (queries shorter than 2 chars are rejected with &#x60;MIN_QUERY_LENGTH&#x60;) - Wildcard &#x60;*&#x60; is accepted and returns all geos - If &#x60;countrycode&#x60; is omitted and &#x60;entity&#x60; is not &#x60;Country&#x60;, defaults to &#x60;US&#x60; 
   * @param xApContext  (required)
   * @param supplySource Supply source context for eligibility evaluation. **Required.** Case-insensitive (e.g., &#x60;maps&#x60;, &#x60;Maps&#x60;, &#x60;MAPS&#x60; are all equivalent). Hard-blocked geos for the specified supply source are always excluded. Soft-blocked geos pass through with eligibility data unless &#x60;eligible&#x3D;true&#x60;.  (required)
   * @param query Query geo search criteria. Minimum 2 characters. Use &#x60;*&#x60; to return all geos. (optional, default to *:*)
   * @param entity Entity based geo search criteria. (optional)
   * @param offset Offset of the first page result to return. (optional, default to 0)
   * @param pageSize Maximum number of entries to return on page. (optional, default to 20)
   * @param countrycode Country code based search criteria (ISO 3166-1 alpha-2). Defaults to US when omitted and entity is not Country. (optional, default to US)
   * @param eligible Controls soft-block filtering. When &#x60;false&#x60; (default), only hard-blocked geos are excluded — soft-blocked geos (low search volume, sparse coverage) remain in results with eligibility data for the UI to handle. When &#x60;true&#x60;, both hard-blocked and soft-blocked geos are excluded.  (optional, default to false)
   * @return Call&lt;GeoSearchResponse&gt;
   */
  @GET("search/geo")
  Call<GeoSearchResponse> searchGeos(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Query("supplySource") SearchSupplySourceType supplySource, @retrofit2.http.Query("query") String query, @retrofit2.http.Query("entity") GeoEntityType entity, @retrofit2.http.Query("offset") Integer offset, @retrofit2.http.Query("pageSize") Integer pageSize, @retrofit2.http.Query("countrycode") String countrycode, @retrofit2.http.Query("eligible") Boolean eligible
  );

  /**
   * Query app search term popularity data
   * Retrieve search term popularity data showing top search terms by search volume within a Country or Region and Genre. Supports WEEKLY and MONTHLY granularity. **Timezone** is fixed to UTC. Maximum **pageSize** is 5000. Maximum **2 sort fields**. Mandatory dimension fields (always returned): countryOrRegion, genre, searchTerm, plus the date field for the selected granularity (week or month). Optional dimension fields: rankInGenre, searchPopularityInGenre, searchPopularity1to100, searchPopularity1to5. 
   * @param xApContext  (required)
   * @param searchTermPopularityQueryRequest  (required)
   * @return Call&lt;SearchTermPopularityQueryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("insights/apps/search-term-popularity/query")
  Call<SearchTermPopularityQueryResponse> searchTermPopularityQuery(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body SearchTermPopularityQueryRequest searchTermPopularityQueryRequest
  );

  /**
   * 
   * Delete SharedBudget by ID
   * @param id  (required)
   * @return Call&lt;Response&gt;
   */
  @DELETE("shared-budgets/{id}")
  Call<Response> sharedBudgetsIdDelete(
    @retrofit2.http.Path("id") String id
  );

  /**
   * 
   * Get SharedBudget by ID
   * @param id  (required)
   * @param xApContext  (optional)
   * @return Call&lt;SharedBudgetResponse&gt;
   */
  @GET("shared-budgets/{id}")
  Call<SharedBudgetResponse> sharedBudgetsIdGet(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Header("X-Ap-Context") String xApContext
  );

  /**
   * 
   * Update SharedBudget
   * @param id  (required)
   * @param sharedBudgetUpdate  (optional)
   * @return Call&lt;SharedBudgetResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("shared-budgets/{id}")
  Call<SharedBudgetResponse> sharedBudgetsIdPut(
    @retrofit2.http.Path("id") String id, @retrofit2.http.Body SharedBudgetUpdate sharedBudgetUpdate
  );

  /**
   * 
   * Create SharedBudget
   * @param sharedBudgetCreate  (optional)
   * @return Call&lt;SharedBudgetResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("shared-budgets")
  Call<SharedBudgetResponse> sharedBudgetsPost(
    @retrofit2.http.Body SharedBudgetCreate sharedBudgetCreate
  );

  /**
   * 
   * Query for SharedBudget
   * @param xApContext  (optional)
   * @param queryRequest  (optional)
   * @return Call&lt;SharedBudgetQueryResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("shared-budgets/query")
  Call<SharedBudgetQueryResponse> sharedBudgetsQueryPost(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Body QueryRequest queryRequest
  );

  /**
   * Update Location Group
   * Updates an existing location group
   * @param xApContext  (required)
   * @param id Business Connect entity identifier (required)
   * @param locationGroupUpdate Updated location group details (required)
   * @return Call&lt;LocationGroupResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("location-groups/{id}")
  Call<LocationGroupResponse> updateLocationGroup(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Path("id") String id, @retrofit2.http.Body LocationGroupUpdate locationGroupUpdate
  );

  /**
   * Upload Asset
   * Uploads a new asset file to the system.  Accepts image files (PNG, JPG, HEIC) via multipart form data. The uploaded file is processed and an Asset record is created with extracted metadata (dimensions, format, file size, etc.). 
   * @param xApContext  (required)
   * @param _file Image file (PNG, JPG, or HEIC) (required)
   * @param promotedObjectId Generic identifier for the promoted object (e.g., brandId for a Business Brand) (required)
   * @param promotedObjectType Type of promoted object. Only BUSINESS_BRAND is supported by this endpoint. (required)
   * @return Call&lt;AssetResponse&gt;
   */
  @retrofit2.http.Multipart
  @POST("assets/upload")
  Call<AssetResponse> uploadAsset(
    @retrofit2.http.Header("X-Ap-Context") String xApContext, @retrofit2.http.Part MultipartBody.Part _file, @retrofit2.http.Part("promotedObjectId") String promotedObjectId, @retrofit2.http.Part("promotedObjectType") String promotedObjectType
  );

}
