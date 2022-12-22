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
import javax.ws.rs.core.Response;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ProductImageAssetController implements ProductImageAssetResource {

    private final StoreService storeService;
    private final ProductService productService;
    private final ProductImageService productImageService;

    @Override
    public Response getPresignedUrl(final boolean preSigned) {
        /*
        if (imageOrder < 1) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("imageOrder must be positive number").build();
        }
        if (StringUtils.isBlank(storeIdentifier)
                || storeService.getStoreInfo(storeIdentifier).isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("invalid storeIdentifier: " + storeIdentifier).build();
        }

        if (StringUtils.isBlank(productIdentifier)
                || productService.findProductInfoFromStore(storeIdentifier, productIdentifier).isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(String.format("Didn't find product %s in storeIdentifier %s.",
                            productIdentifier, storeIdentifier)).build();
        }
        */
        log.info("here################");

        if (preSigned) {
            log.info("getS3PreSignedUrl");
            final String preSignedUrl = productImageService
                    .getS3PreSignedUrl("AAA", "111", 1);
            return Response.status(Response.Status.OK)
                    .entity( Map.entry("presignedUrl", preSignedUrl)).build();
        } else {
            log.info("not provided presigned parameter");
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
