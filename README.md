# AppiumSeleniumProxy
Connect Selenium / Appium java tests to remote grid through a corporate proxy.

The provided code was tested with appium java client version 7.1.0.

In the following example you can see how it can be used to create an IOSDriver.

````
    static String host = "<env>.experitest.com";
    static String accessKey = "<access key>";
    static String proxyHost = "<proxy host>";
    static int proxyPort = 8080;
    static String proxyUser = "user";
    static String proxyPassword = "password";

    RemoteWebDriver driver = null;
    protected DesiredCapabilities dc = new DesiredCapabilities();
    @Before
    public void setUp() throws Exception{
        dc.setCapability("deviceQuery", "@os='ios'");
        dc.setCapability("accessKey", accessKey);

        dc.setCapability("platformName", "ios");

        URL url = new URL("https://" + host + "/wd/hub");

        //HttpCommandExecutor executor = new HttpCommandExecutor(new HashMap<String, CommandInfo>(), url, new MyFactory());
        driver = new IOSDriver<IOSElement>(
                SeleniumClientFactory.createExecutor(url, proxyHost, proxyPort , proxyUser, proxyPassword), dc);
    }

````
