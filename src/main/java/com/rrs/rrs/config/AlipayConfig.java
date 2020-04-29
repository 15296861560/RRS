package com.rrs.rrs.config;

public class AlipayConfig {

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016102200741274";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCRv5k10rIvabDiJj8z69pL6W3MEqJtXAR4XxNn4a53bZ3SLnPdE0Fj8nV7dRkbi0MAD20eZd1lQcry0VeGhQ7prCwIfGtooE5WNg+UIsYbMJS6CV5aqZ3JsoOVHmS14f+kJU6C+hLsvUEB7bx5+NJOP7Ta71dBQ1soIQvBRO43tnCnuh6e/Hvdvwkn6PsCeuPoAYcIbP+ZzxMxCXPVZX5xWZy3VY1hclGmVrAMSUvJxW5ETaM8334Mib/mF4lp2ZJYwkaI0+NU9k7Q+MrQhoApgs8P9zAdN6w+OI589xyjj4O9Y7bMlVRvgEI6hqgX1N3DnbjGtoLOKDUeS7xQYtrJAgMBAAECggEABFLCoK4XSwC/lnW7+jZMy2OpNhx3ZPcNyuRHFxXCrxg5TjrChRRFJEnU4kye6go/eoj6m6Q55P8Gnh1Hk13vC09oHvhR11CqAjSoUaSWa8gNIVeVzOqrG4iIDqxQIHULuDrDEW+JkCws+Xmr1ZJj6p5JJxViaTUuRwpz5s+8oTpkblSELAjgDzkYal2mFFikT5dLljRDd+lv9IwaOGeDgiWCLrn2GxIyIjRpfsk0CVcnaumn3i+3MNKC43Hg/W+9xu1D0Ub1bDhno14YA+Eyoe5aV8FU1+7UertbJ8gxTJ/E7oATHw5YgRY6eDFu/QxOZ5J9njhWg9Im3AHAujszgQKBgQDxXZYkBjYuSxyh+1X2w5LHlu5IaydP4EmXltSHJMCYTSUy8QBCm9Z8D8m52Q7tTWjVM0ZEPYox+b8siK7WoJvECjfB+6Dfoefc2VRuLtfQ2BJfFnw36Yql1KMH+4eXvDTs/rh9CWwBxBJetfSqbLT8bEEhKZEt9khtlhFniL+u9QKBgQCald4eV9i6EfSfm3JMrKOL9PIq7VuW8cOoHVwQ+A27yGqf/7rYVNhAdyMf6zwauhkTAAgO88ywFbpI1e72TNUWHruruRSiW+b1KGbQQaeBmw3mNycjOpENlMyV5RO48b+FEC6zB3ISeyKfP5Q0pnoGwaWnUp3cMmMdG6MOuWywBQKBgGf4v39fKaHBe2EPipVU4/qUOqr5daLD5A+nyn84xroUqUyLYmhPBxLEHkqvhO2WJe2JO+CTfd9PTRna+y27W74UBLbMNR2Aege5mtQn25G8MhXB8tqHANG0ilIa+OrVCRt6EnDSSTNtidhAm1d6onAGFfw6Eq7w43xJY8/sqV2tAoGAOlw2HTKBCc5ZkiA3iKbsn5v8CSTPHS9s9H0REv7zAF/rkTgUrDNr6PqRci125Yg4iynJ4Qs9zYSGbZUGwEnFcLJtm6kmFx2nFgsItbrzqESHfNB5oB0uQfhZPIXdakcqTOwNFV3MPRnruj9gjngDPaXCCmdwXX9QV97lMlviopkCgYEA1Pmm10vRID75n2Kh8Dtc2QS7ZC6GEOhF2l4OphPOMhpnehsYjoVYphUbi6iBJdm9XxMiU0VQOfBuo4Y6zFkcLiG5eftEcyy0sSMarIIFaOYAQyPYWk4RWU/EnvJiM9C1579GNHlslHDDGYs4JSz+u+SsjuaA/BxWoBUavxSrzaA=";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm
    // 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAssAjSheGcechiOM5ZALJMpdm4qKuxhBSr2N/1tFXQdsc0cNn3e0z5dMG+NL/NQFtZXTTc5XMQ4OTDXVTGgl0TplI7KhptLPczteFk2xQNqPN9z8jHD8htecxiTeczRvVnI9p6QFWZApfbfer4Jd8n6pBl2mDTJ7HgoC3hZ7oPw61CEyNIzi/5lwPA5iT1uPxrnZTkvLPb0WuuxeVkUJ/xMsizyhQGPDpLClY1WWgnmUhEny/eMT2CX6vhLYa/WVSdbi5k0lucp9S61Y4wr1Va8t8JtA6N3/znhTeNTYHgBIgba7QjyEvEooslbUa+KFABdKl4LvUfhxOVbxSwtj21QIDAQAB";
    // 服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    /**
     * 返回的时候此页面不会返回到用户页面，只会执行你写到控制器里的地址
     */
    public static String notify_url = "http://localhost:8888/notifyUrl";
    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    /**
     * 此页面是同步返回用户页面，也就是用户支付后看到的页面，上面的notify_url是异步返回商家操作，谢谢
     */
    public static String return_url = "http://localhost:8888/returnUrl";
    // 签名方式
    public static String sign_type = "RSA2";
    // 字符编码格式
    public static String charset = "utf-8";
    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
    // 日志地址
    //public static String log_path = "D:/logs/";


}

