package com.ef.bite.dataacces.mode.httpMode;

import java.util.List;

public class HttpServerAddress extends HttpBaseMessage {


    /**
     * data : {"enable_forget_password":true,"domain":"http://10.128.34.182","login_providers":["wechat"],"study_plans":[{"plan_id":"beginner","order":1},{"plan_id":"sequence","order":2}]}
     * message : null
     * status : 0
     */
    private DataEntity data;


    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public class DataEntity {
        /**
         * enable_forget_password : true
         * domain : http://10.128.34.182
         * login_providers : ["wechat"]
         * study_plans : [{"plan_id":"beginner","order":1},{"plan_id":"sequence","order":2}]
         */
        private boolean enable_forget_password;
        private String domain;
        private List<String> login_providers;
        private List<Study_plansEntity> study_plans;

        public void setEnable_forget_password(boolean enable_forget_password) {
            this.enable_forget_password = enable_forget_password;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public void setLogin_providers(List<String> login_providers) {
            this.login_providers = login_providers;
        }

        public void setStudy_plans(List<Study_plansEntity> study_plans) {
            this.study_plans = study_plans;
        }

        public boolean isEnable_forget_password() {
            return enable_forget_password;
        }

        public String getDomain() {
            return domain;
        }

        public List<String> getLogin_providers() {
            return login_providers;
        }

        public List<Study_plansEntity> getStudy_plans() {
            return study_plans;
        }

        public class Study_plansEntity {
            /**
             * plan_id : beginner
             * order : 1
             */
            private String plan_id;
            private int order;

            public void setPlan_id(String plan_id) {
                this.plan_id = plan_id;
            }

            public void setOrder(int order) {
                this.order = order;
            }

            public String getPlan_id() {
                return plan_id;
            }

            public int getOrder() {
                return order;
            }
        }
    }
}
