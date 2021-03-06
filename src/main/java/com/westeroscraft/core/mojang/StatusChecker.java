package com.westeroscraft.core.mojang;
 
public enum StatusChecker {
    ACCOUNTS("Accounts Service", "account.mojang.com"),
    AUTHENTICATION("Authentication Service", "auth.mojang.com"),
    AUTHENTICATION_SERVER("Authentication Server", "authserver.mojang.com"),
    LOGIN("Login Service", "login.minecraft.net"),
    SESSION_MINECRAFT("Minecraft Session Server", "session.minecraft.net"),
    SESSION_MOJANG("Mojang Session Server", "sessionserver.mojang.com"),
    SKINS("Skin Server", "skins.minecraft.net"),
    MAIN_WEBSITE("Main Site", "minecraft.net");
 
    /*
    * @Author TheTinySpider
    * Idea from: sebasju1234
    *
    * Status names and other information can be found at:
    * [url]http://minecraft.gamepedia.com/User_talk:Oxguy3/Minecraft.net_API[/url]
    */
 
    private String name, serviceURL;
//    private JSONParser jsonParser = new JSONParser();

    StatusChecker(String name, String serviceURL) {
        this.name = name;
        this.serviceURL = serviceURL;
    }
 
    public String getName() {
        return name;
    }
 
//    /**
//    * Check the current Mojang service for it's status, errors are ignored.
//    *
//    * @return Status of the service.
//    */
//    public Status getStatus() {
//        return getStatus(true);
//    }
//
//    /**
//    * Check the current Mojang service for it's status.
//    *
//    * @param suppressErrors - Don't print errors in console.
//    * @return Status of the service.
//    */
//    public Status getStatus(boolean suppressErrors) {
//        try {
//            URL url = new URL("http://status.mojang.com/check?service=" + serviceURL);
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
//
//            Object object = jsonParser.parse(bufferedReader);
//            JSONObject jsonObject = (JSONObject) object;
//
//            String status = (String) jsonObject.get(serviceURL);
//
//            return Status.get(status);
//
//        } catch (IOException | ParseException exception) {
//
//            if (!suppressErrors) {
//                exception.printStackTrace();
//            }
//
//            return Status.UNKNOWN;
//        }
//    }

    public enum Status {
        ONLINE("Online"),
        UNSTABLE("Unstable"),
        OFFLINE("Offline"),
        UNKNOWN("Unknown");
 
        private String status;
 
        Status(String status) {
            this.status = status;
        }
 
        public String getStatus() {
            return status;
        }
 
        public static Status get(String status) {
            status = status.toLowerCase();
 
            switch (status) {
            case "green":
                return Status.ONLINE;
            case "yellow":
                return Status.UNSTABLE;
            case "red":
                return Status.OFFLINE;
            default:
                return Status.UNKNOWN;
            }
        }
    }
}