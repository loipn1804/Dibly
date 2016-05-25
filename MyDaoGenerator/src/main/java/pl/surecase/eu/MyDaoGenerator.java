package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MyDaoGenerator {

    public static Schema schema;

    public static void main(String args[]) throws Exception {
        //current is 3 need to update to 4
        schema = new Schema(8, "greendao");

        Entity customer = schema.addEntity("ObjectUser");
        customer.addLongProperty("user_id").primaryKey();
        customer.addLongProperty("group_id");
        customer.addStringProperty("email");
        customer.addBooleanProperty("isConsumer");
        customer.addLongProperty("consumer_id");
        customer.addStringProperty("first_name");
        customer.addStringProperty("last_name");
        customer.addStringProperty("phone");
        customer.addStringProperty("phoneCode");
        customer.addStringProperty("gender");
        customer.addStringProperty("profile_image");
        customer.addStringProperty("facebook_id");
        customer.addStringProperty("dob");
        customer.addIntProperty("k_following");
        customer.addIntProperty("k_call_dibs");
        customer.addStringProperty("full_name");
        customer.addStringProperty("ref_code");
        customer.addIntProperty("f_verified_phone");
        customer.addIntProperty("k_ref_bonus");
        customer.addIntProperty("k_ref_user");
        customer.addIntProperty("f_valid");

        Entity deal = schema.addEntity("ObjectDeal");
        deal.addIdProperty().autoincrement();
        deal.addLongProperty("deal_id");
        deal.addStringProperty("image");
        deal.addStringProperty("image_thumbnail");
        deal.addStringProperty("title");
        deal.addStringProperty("type");
        deal.addStringProperty("start_at");
        deal.addStringProperty("end_at");
        deal.addIntProperty("max_claim");
        deal.addFloatProperty("original_price");
        deal.addFloatProperty("purchase_now_price");
        deal.addLongProperty("outlet_id");
        deal.addLongProperty("merchant_id");
        deal.addIntProperty("deal_type");
        deal.addStringProperty("description");
        deal.addStringProperty("terms");
        deal.addBooleanProperty("f_claimed");
        deal.addBooleanProperty("f_call_dibs");
        deal.addStringProperty("duration_type");
        deal.addIntProperty("k_rest_claim");
        deal.addStringProperty("outlets");
        deal.addIntProperty("k_likes");
        deal.addIntProperty("k_unlikes");
        deal.addBooleanProperty("f_yay");
        deal.addBooleanProperty("f_nay");
        deal.addStringProperty("claim_validity");
        deal.addStringProperty("organization_name");
        deal.addBooleanProperty("is_exclusive");
        deal.addIntProperty("k_deals_by_outlet");
        deal.addStringProperty("refer_deal_ids");
        deal.addBooleanProperty("f_liked");
        deal.addStringProperty("logo_image_url");
        deal.addLongProperty("ref_merchant_id");
        deal.addBooleanProperty("fromDealFollowing");
        deal.addStringProperty("uuid");
        deal.addIntProperty("group_id");
        deal.addStringProperty("group_name");
        Property boxId = deal.addLongProperty("dealReferId").getProperty();
        ToMany boxToItem = deal.addToMany(deal, boxId);
        boxToItem.setName("refer_deal_infos");


        Entity deal_outlet = schema.addEntity("ObjectDealOutlet");
        deal_outlet.addIdProperty().autoincrement();
        deal_outlet.addLongProperty("outlet_id");
        deal_outlet.addLongProperty("deal_id");
        deal_outlet.addStringProperty("latitude");
        deal_outlet.addStringProperty("longitude");
        deal_outlet.addFloatProperty("distance");
        deal_outlet.addStringProperty("name");
        deal_outlet.addStringProperty("phone");
        deal_outlet.addStringProperty("address1");
        deal_outlet.addStringProperty("address2");

        Entity deal_merchant = schema.addEntity("ObjectDealMerchant");
        deal_merchant.addIdProperty().autoincrement();
        deal_merchant.addLongProperty("merchant_id");
        deal_merchant.addLongProperty("deal_id");
        deal_merchant.addStringProperty("organization_name");
        deal_merchant.addStringProperty("logo_image_url");

        Entity merchant = schema.addEntity("Merchant");
        merchant.addLongProperty("merchant_id").primaryKey();
        merchant.addStringProperty("first_name");
        merchant.addStringProperty("last_name");
        merchant.addStringProperty("organization_name");
        merchant.addIntProperty("industry_type_id");
        merchant.addStringProperty("phone");
        merchant.addStringProperty("tags");
        merchant.addStringProperty("logo_image");
        merchant.addIntProperty("k_follows");
        merchant.addIntProperty("k_likes");
        merchant.addIntProperty("k_unlikes");
        merchant.addIntProperty("k_outlets");
        merchant.addBooleanProperty("f_yay");
        merchant.addBooleanProperty("f_nay");
        merchant.addBooleanProperty("f_follow");
        merchant.addStringProperty("profile_images");// json
        merchant.addIntProperty("live_deals");
        merchant.addIntProperty("past_deals");
        merchant.addStringProperty("website_url");
        merchant.addStringProperty("facebook_url");
        merchant.addStringProperty("twitter_url");
        merchant.addStringProperty("instagram_url");
        merchant.addStringProperty("description");
        merchant.addStringProperty("cover_image");

        Entity merchant_list = schema.addEntity("MerchantList");
        merchant_list.addIdProperty().autoincrement();
        merchant_list.addLongProperty("merchant_id");
        merchant_list.addStringProperty("organization_name");
        merchant_list.addStringProperty("website_url");
        merchant_list.addStringProperty("logo_image");
        merchant_list.addStringProperty("industry_name");
        merchant_list.addStringProperty("cover_image");
        merchant_list.addIntProperty("k_live_deals");
        merchant_list.addBooleanProperty("f_follow");
        merchant_list.addIntProperty("merchant_type");

        Entity outlet = schema.addEntity("Outlet");
        outlet.addLongProperty("outlet_id").primaryKey();
        outlet.addLongProperty("merchant_id");
        outlet.addStringProperty("name");
        outlet.addStringProperty("phone");
        outlet.addStringProperty("address1");
        outlet.addStringProperty("address2");
        outlet.addStringProperty("zip_code");
        outlet.addStringProperty("latitude");
        outlet.addStringProperty("longitude");
        outlet.addStringProperty("secret_code");

        Entity comment = schema.addEntity("Comment");
        comment.addLongProperty("comment_id").primaryKey();
        comment.addStringProperty("text");
        comment.addStringProperty("created_at");
        comment.addLongProperty("consumer_id");
        comment.addStringProperty("first_name");
        comment.addStringProperty("last_name");
        comment.addStringProperty("profile_image");
        comment.addLongProperty("deal_id");
        comment.addStringProperty("title");

        Entity myLastLocation = schema.addEntity("MyLocation");
        myLastLocation.addIdProperty().autoincrement();
        myLastLocation.addDoubleProperty("latitude");
        myLastLocation.addDoubleProperty("longitude");
        myLastLocation.addDoubleProperty("distance");
        myLastLocation.addStringProperty("lastUpdateTime");

        Entity myDiscovery = schema.addEntity("Discovery");
        myDiscovery.addLongProperty("id").primaryKey();
        myDiscovery.addStringProperty("name");
        myDiscovery.addStringProperty("imageLink");
        myDiscovery.addIntProperty("position");
        myDiscovery.addIntProperty("num_deal");

        Entity deal_avai = schema.addEntity("DealAvailable");
        deal_avai.addLongProperty("deal_id").primaryKey();
        deal_avai.addStringProperty("title");
        deal_avai.addStringProperty("start_at");
        deal_avai.addStringProperty("end_at");
        deal_avai.addFloatProperty("purchase_now_price");
        deal_avai.addLongProperty("merchant_id");
        deal_avai.addStringProperty("organization_name");
        deal_avai.addStringProperty("uuid");
        deal_avai.addStringProperty("type");
        deal_avai.addStringProperty("secret_code");
        deal_avai.addStringProperty("validity");
        deal_avai.addStringProperty("outlet_name");

        Entity deal_claimed = schema.addEntity("DealClaimed");
        deal_claimed.addLongProperty("deal_id").primaryKey();
        deal_claimed.addStringProperty("title");
        deal_claimed.addStringProperty("created_at");
        deal_claimed.addStringProperty("consumed_at");
        deal_claimed.addLongProperty("merchant_id");
        deal_claimed.addStringProperty("organization_name");
        deal_claimed.addStringProperty("validity");
        deal_claimed.addBooleanProperty("f_claimed");

        Entity filter = schema.addEntity("DealFilter");
        filter.addIdProperty();
        filter.addStringProperty("purchase_type");
        filter.addLongProperty("distance");
        filter.addStringProperty("industry_type");
        filter.addStringProperty("keyword");

        Entity dealSearch = schema.addEntity("ObjectDealSearch");
        dealSearch.addIdProperty().autoincrement();
        dealSearch.addLongProperty("deal_id");
        dealSearch.addStringProperty("image");
        dealSearch.addStringProperty("title");
        dealSearch.addStringProperty("type");
        dealSearch.addStringProperty("start_at");
        dealSearch.addStringProperty("end_at");
        dealSearch.addIntProperty("max_claim");
        dealSearch.addFloatProperty("original_price");
        dealSearch.addFloatProperty("purchase_now_price");
        dealSearch.addLongProperty("outlet_id");
        dealSearch.addLongProperty("merchant_id");
        dealSearch.addIntProperty("is_longterm");
        dealSearch.addStringProperty("description");
        dealSearch.addStringProperty("terms");
        dealSearch.addBooleanProperty("f_claimed");
        dealSearch.addBooleanProperty("f_call_dibs");

        Entity following = schema.addEntity("Following");
        following.addIdProperty().autoincrement();
        following.addLongProperty("merchant_id");
        following.addStringProperty("merchant_name");
        following.addIntProperty("num_of_new_deals");
        following.addStringProperty("logo_image_url");
        following.addStringProperty("cover_image_url");

        Entity typeSearch = schema.addEntity("TypeSearch");
        typeSearch.addLongProperty("id").primaryKey();
        typeSearch.addStringProperty("name");

        Entity industryType = schema.addEntity("IndustryType");
        industryType.addLongProperty("id").primaryKey();
        industryType.addStringProperty("name");

        Entity industrySearch = schema.addEntity("IndustrySearch");
        industrySearch.addLongProperty("id").primaryKey();

        Entity discount = schema.addEntity("Discount");
        discount.addLongProperty("id").primaryKey();
        discount.addStringProperty("code");
        discount.addFloatProperty("amount");
        discount.addStringProperty("note");
        discount.addStringProperty("created_at");

        // [1:56:33 PM] Binh Huynh:
        Entity permanentDeals = schema.addEntity("PermanentDeals");
        permanentDeals.addIdProperty().autoincrement();
        permanentDeals.addLongProperty("deal_id");
        permanentDeals.addStringProperty("image");
        permanentDeals.addStringProperty("title");
        permanentDeals.addStringProperty("type");
        permanentDeals.addStringProperty("start_at");
        permanentDeals.addStringProperty("end_at");
        permanentDeals.addIntProperty("max_claim");
        permanentDeals.addFloatProperty("original_price");
        permanentDeals.addFloatProperty("purchase_now_price");
        permanentDeals.addLongProperty("outlet_id");
        permanentDeals.addLongProperty("merchant_id");
        permanentDeals.addIntProperty("deal_type");
        permanentDeals.addStringProperty("description");
        permanentDeals.addStringProperty("terms");
        permanentDeals.addBooleanProperty("f_claimed");
        permanentDeals.addBooleanProperty("f_call_dibs");
        permanentDeals.addDoubleProperty("latitude");
        permanentDeals.addDoubleProperty("longitude");
        permanentDeals.addIntProperty("isNotified");

        Entity notify = schema.addEntity("Notify");
        notify.addLongProperty("deal_id").primaryKey();

        upgradeToVersion4();

        new DaoGenerator().generateAll(schema, args[0]);
    }

    public static void upgradeToVersion4() {
        Entity permanentDeals = schema.addEntity("PhoneCode");
        permanentDeals.addIntProperty("id");
        permanentDeals.addStringProperty("code");
        permanentDeals.addStringProperty("name");
        permanentDeals.addStringProperty("dial_code");
        permanentDeals.addStringProperty("f_disabled");

        // Category
        Entity category = schema.addEntity("Category");
        category.addIntProperty("id");
        category.addStringProperty("name");
        category.addIntProperty("position");
        category.addBooleanProperty("isSelect");

        // Type Deal
        Entity typeDeal = schema.addEntity("TypeDeal");
        typeDeal.addIntProperty("id");
        typeDeal.addStringProperty("text");
        typeDeal.addIntProperty("order");
        typeDeal.addBooleanProperty("isSelect");
        typeDeal.addIntProperty("f_deleted");

        Entity review = schema.addEntity("Review");
        review.addIdProperty().autoincrement();
        review.addLongProperty("review_id");
        review.addLongProperty("consumer_id");
        review.addLongProperty("deal_id");
        review.addLongProperty("merchant_id");
        review.addStringProperty("review");
        review.addBooleanProperty("is_yay");
        review.addStringProperty("created_at");
        review.addStringProperty("updated_at");
        review.addStringProperty("fullname");
        review.addStringProperty("profile_image");

    }

    // sample
    public static void gen1box_manyitems() {

        Entity box = schema.addEntity("Box");
        box.addIdProperty();
        box.addStringProperty("name");
        box.addIntProperty("slots");
        box.addStringProperty("description");

        Entity item = schema.addEntity("Item");
        Property itemId = item.addIdProperty().getProperty();
        item.addStringProperty("name");
        item.addIntProperty("quantity");

        Property boxId = item.addLongProperty("boxId").getProperty();
        ToMany boxToItem = box.addToMany(item, boxId);
        boxToItem.orderDesc(itemId);

    }


    // sample
    public static void gen1box1item() {

        Entity box = schema.addEntity("Box");
        box.addIdProperty();
        box.addStringProperty("name");
        box.addIntProperty("slots");
        box.addStringProperty("description");

        Entity item = schema.addEntity("Item");
        item.addIdProperty();
        item.addStringProperty("name");
        item.addIntProperty("quantity");

        Property itemId = box.addLongProperty("itemId").getProperty();
        box.addToOne(item, itemId);

    }
}
