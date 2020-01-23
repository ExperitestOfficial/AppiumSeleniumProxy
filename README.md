# AppiumSeleniumProxy
Connect Selenium / Appium java tests to remote grid through a corporate proxy.

The provided code was tested with appium java client version 7.1.0.

In the following example you can see how it can be used to create an IOSDriver.

```java
    static String host = "<env>.experitest.com";
    static String accessKey = "<access key>";
    static String proxyHost = "<proxy host>";
    static int proxyPort = 8080;
    static String proxyUser = "user";
    static String proxyPassword = "password";

    IOSDriver driver = null;
    protected DesiredCapabilities dc = new DesiredCapabilities();
    @Before
    public void setUp() throws Exception{
        dc.setCapability("deviceQuery", "@os='ios'");
        dc.setCapability("accessKey", accessKey);

        dc.setCapability("platformName", "ios");

        URL url = new URL("https://" + host + "/wd/hub");

        driver = new IOSDriver<IOSElement>(
                SeleniumClientFactory.createExecutor(url, proxyHost, proxyPort , proxyUser, proxyPassword), dc);
    }

```

If you are getting the following exception:
```
Caused by: javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
```
it is due to MIDM proxy used by your corporate and you should follow the following guidlines:
https://stackoverflow.com/questions/9619030/resolving-javax-net-ssl-sslhandshakeexception-sun-security-validator-validatore

To get access to mobile devices and desktop browser:

https://experitest.com



