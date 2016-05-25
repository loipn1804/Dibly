package com.dibs.dibly.api;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by USER on 5/13/2015.
 */
public class APIWrapper {


    public static boolean login(Context context, String username, String pass) {
        boolean isLoginSuccess = false;
        API api = new API();
        String result = api.login(username, pass);
        JSONObject jsonResponse;

//        try
//        {
//            jsonResponse = new JSONObject(result);
//
//            JSONObject root = jsonResponse.getJSONObject("root");
//
//            String statusFromServer = root.optString("status");
//
//            if (statusFromServer.equalsIgnoreCase("success"))
//            {
//                JSONObject data = root.getJSONObject("data");
//                String groupName = data.optString("group_name");
//                String customerName = data.optString("customer_name");
//                String customerCode = data.optString("customer_code");
//                String email = data.optString("email");
//                Double loyaltyPoint = data.optDouble("points");
//                String customerID = data.optString("customer_id");
//
//                JSONObject company = data.getJSONObject("store");
//                String companyName = company.optString("store_name");
//                Double minAmount = company.optDouble("voucher_min_amount");
//                Double maxAmount = company.optDouble("voucher_max_amount");
//                Boolean isFixedAmount = company.optBoolean("voucher_is_fixed_amount");
//
//
//                Customer customer = new Customer();
//                customer.setId(1l);
//                customer.setCustomerID(customerID);
//                customer.setCustomerName(customerName);
//                customer.setCustomerEmail(email);
//                //if (!loyaltyPoint.isEmpty())
//                //{
//                customer.setCustomerPoint(loyaltyPoint);
//                // }
//
//                customer.setCustomerGroup(groupName);
//                customer.setCustomerCode(customerCode);
//                customer.setCompany(companyName);
//                customer.setVoucherMin(minAmount);
//                customer.setVoucherMax(maxAmount);
//                customer.setIsFixedAmount(isFixedAmount);
//                customer.setFixedAmount(10.0);
//
//
//                List<Customer> customers = GreenDaoController.getCustomerByCustomerID(context, customerID);
//
//
//                if (customers.size() > 0)
//                {
//                    customer.setId(customers.get(0).getId());
//                    GreenDaoController.updateCustomer(context, customer);
//                }
//                else
//                {
//                    GreenDaoController.insertCustomer(context, customer);
//                }
//
//
//                isLoginSuccess = true;
//            }
//            else
//            {
//                isLoginSuccess = false;
//            }
//        }
//        catch (Exception ex)
//        {
//            isLoginSuccess = false;
//        }

        return isLoginSuccess;
    }


}
