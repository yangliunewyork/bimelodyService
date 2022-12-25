package com.bimelody.ecommerceservice.controller;

import com.bimelody.ecommerceservice.resource.ProductImageAssetResource;
import com.bimelody.ecommerceservice.service.ProductImageService;
import com.bimelody.ecommerceservice.service.ProductService;
import com.bimelody.ecommerceservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import javax.inject.Qualifier;
import javax.ws.rs.core.Response;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ProductImageAssetController implements ProductImageAssetResource {

    private final StoreService storeService;
    private final ProductService productService;
    private final ProductImageService productImageService;


    @Override
    public Response getPresignedUrl(String storeIdentifier, String imageName, int imageOrder) {

        final String preSignedUrl = productImageService
                .getS3PreSignedUrl(storeIdentifier, imageName, imageOrder);
        return Response.status(Response.Status.OK)
                .entity(Map.entry("presignedUrl", preSignedUrl)).build();
    }

}
