
package businesslayer.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the businesslayer.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetAgriturByQueryResponse_QNAME = new QName("http://ws.businesslayer/", "getAgriturByQueryResponse");
    private final static QName _GetDetailedAgritur_QNAME = new QName("http://ws.businesslayer/", "getDetailedAgritur");
    private final static QName _GetDetailedAgriturResponse_QNAME = new QName("http://ws.businesslayer/", "getDetailedAgriturResponse");
    private final static QName _GetNearAgriturResponse_QNAME = new QName("http://ws.businesslayer/", "getNearAgriturResponse");
    private final static QName _RecommendAgritur_QNAME = new QName("http://ws.businesslayer/", "recommendAgritur");
    private final static QName _RecommendAgriturResponse_QNAME = new QName("http://ws.businesslayer/", "recommendAgriturResponse");
    private final static QName _UserMarkAgriturResponse_QNAME = new QName("http://ws.businesslayer/", "userMarkAgriturResponse");
    private final static QName _GetAgriturByPlace_QNAME = new QName("http://ws.businesslayer/", "getAgriturByPlace");
    private final static QName _UserViewAgriturResponse_QNAME = new QName("http://ws.businesslayer/", "userViewAgriturResponse");
    private final static QName _GetNearAgritur_QNAME = new QName("http://ws.businesslayer/", "getNearAgritur");
    private final static QName _Agritur_QNAME = new QName("http://ws.businesslayer/", "agritur");
    private final static QName _UserViewAgritur_QNAME = new QName("http://ws.businesslayer/", "userViewAgritur");
    private final static QName _GetAgriturByPlaceResponse_QNAME = new QName("http://ws.businesslayer/", "getAgriturByPlaceResponse");
    private final static QName _UserMarkAgritur_QNAME = new QName("http://ws.businesslayer/", "userMarkAgritur");
    private final static QName _GetAgriturByQuery_QNAME = new QName("http://ws.businesslayer/", "getAgriturByQuery");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: businesslayer.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetAgriturByPlaceResponse }
     * 
     */
    public GetAgriturByPlaceResponse createGetAgriturByPlaceResponse() {
        return new GetAgriturByPlaceResponse();
    }

    /**
     * Create an instance of {@link UserViewAgritur }
     * 
     */
    public UserViewAgritur createUserViewAgritur() {
        return new UserViewAgritur();
    }

    /**
     * Create an instance of {@link Agritur }
     * 
     */
    public Agritur createAgritur() {
        return new Agritur();
    }

    /**
     * Create an instance of {@link GetAgriturByQuery }
     * 
     */
    public GetAgriturByQuery createGetAgriturByQuery() {
        return new GetAgriturByQuery();
    }

    /**
     * Create an instance of {@link UserMarkAgritur }
     * 
     */
    public UserMarkAgritur createUserMarkAgritur() {
        return new UserMarkAgritur();
    }

    /**
     * Create an instance of {@link GetNearAgritur }
     * 
     */
    public GetNearAgritur createGetNearAgritur() {
        return new GetNearAgritur();
    }

    /**
     * Create an instance of {@link RecommendAgriturResponse }
     * 
     */
    public RecommendAgriturResponse createRecommendAgriturResponse() {
        return new RecommendAgriturResponse();
    }

    /**
     * Create an instance of {@link UserMarkAgriturResponse }
     * 
     */
    public UserMarkAgriturResponse createUserMarkAgriturResponse() {
        return new UserMarkAgriturResponse();
    }

    /**
     * Create an instance of {@link UserViewAgriturResponse }
     * 
     */
    public UserViewAgriturResponse createUserViewAgriturResponse() {
        return new UserViewAgriturResponse();
    }

    /**
     * Create an instance of {@link GetAgriturByPlace }
     * 
     */
    public GetAgriturByPlace createGetAgriturByPlace() {
        return new GetAgriturByPlace();
    }

    /**
     * Create an instance of {@link GetAgriturByQueryResponse }
     * 
     */
    public GetAgriturByQueryResponse createGetAgriturByQueryResponse() {
        return new GetAgriturByQueryResponse();
    }

    /**
     * Create an instance of {@link GetDetailedAgritur }
     * 
     */
    public GetDetailedAgritur createGetDetailedAgritur() {
        return new GetDetailedAgritur();
    }

    /**
     * Create an instance of {@link GetDetailedAgriturResponse }
     * 
     */
    public GetDetailedAgriturResponse createGetDetailedAgriturResponse() {
        return new GetDetailedAgriturResponse();
    }

    /**
     * Create an instance of {@link GetNearAgriturResponse }
     * 
     */
    public GetNearAgriturResponse createGetNearAgriturResponse() {
        return new GetNearAgriturResponse();
    }

    /**
     * Create an instance of {@link RecommendAgritur }
     * 
     */
    public RecommendAgritur createRecommendAgritur() {
        return new RecommendAgritur();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAgriturByQueryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.businesslayer/", name = "getAgriturByQueryResponse")
    public JAXBElement<GetAgriturByQueryResponse> createGetAgriturByQueryResponse(GetAgriturByQueryResponse value) {
        return new JAXBElement<GetAgriturByQueryResponse>(_GetAgriturByQueryResponse_QNAME, GetAgriturByQueryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDetailedAgritur }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.businesslayer/", name = "getDetailedAgritur")
    public JAXBElement<GetDetailedAgritur> createGetDetailedAgritur(GetDetailedAgritur value) {
        return new JAXBElement<GetDetailedAgritur>(_GetDetailedAgritur_QNAME, GetDetailedAgritur.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDetailedAgriturResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.businesslayer/", name = "getDetailedAgriturResponse")
    public JAXBElement<GetDetailedAgriturResponse> createGetDetailedAgriturResponse(GetDetailedAgriturResponse value) {
        return new JAXBElement<GetDetailedAgriturResponse>(_GetDetailedAgriturResponse_QNAME, GetDetailedAgriturResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNearAgriturResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.businesslayer/", name = "getNearAgriturResponse")
    public JAXBElement<GetNearAgriturResponse> createGetNearAgriturResponse(GetNearAgriturResponse value) {
        return new JAXBElement<GetNearAgriturResponse>(_GetNearAgriturResponse_QNAME, GetNearAgriturResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RecommendAgritur }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.businesslayer/", name = "recommendAgritur")
    public JAXBElement<RecommendAgritur> createRecommendAgritur(RecommendAgritur value) {
        return new JAXBElement<RecommendAgritur>(_RecommendAgritur_QNAME, RecommendAgritur.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RecommendAgriturResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.businesslayer/", name = "recommendAgriturResponse")
    public JAXBElement<RecommendAgriturResponse> createRecommendAgriturResponse(RecommendAgriturResponse value) {
        return new JAXBElement<RecommendAgriturResponse>(_RecommendAgriturResponse_QNAME, RecommendAgriturResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserMarkAgriturResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.businesslayer/", name = "userMarkAgriturResponse")
    public JAXBElement<UserMarkAgriturResponse> createUserMarkAgriturResponse(UserMarkAgriturResponse value) {
        return new JAXBElement<UserMarkAgriturResponse>(_UserMarkAgriturResponse_QNAME, UserMarkAgriturResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAgriturByPlace }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.businesslayer/", name = "getAgriturByPlace")
    public JAXBElement<GetAgriturByPlace> createGetAgriturByPlace(GetAgriturByPlace value) {
        return new JAXBElement<GetAgriturByPlace>(_GetAgriturByPlace_QNAME, GetAgriturByPlace.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserViewAgriturResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.businesslayer/", name = "userViewAgriturResponse")
    public JAXBElement<UserViewAgriturResponse> createUserViewAgriturResponse(UserViewAgriturResponse value) {
        return new JAXBElement<UserViewAgriturResponse>(_UserViewAgriturResponse_QNAME, UserViewAgriturResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNearAgritur }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.businesslayer/", name = "getNearAgritur")
    public JAXBElement<GetNearAgritur> createGetNearAgritur(GetNearAgritur value) {
        return new JAXBElement<GetNearAgritur>(_GetNearAgritur_QNAME, GetNearAgritur.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Agritur }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.businesslayer/", name = "agritur")
    public JAXBElement<Agritur> createAgritur(Agritur value) {
        return new JAXBElement<Agritur>(_Agritur_QNAME, Agritur.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserViewAgritur }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.businesslayer/", name = "userViewAgritur")
    public JAXBElement<UserViewAgritur> createUserViewAgritur(UserViewAgritur value) {
        return new JAXBElement<UserViewAgritur>(_UserViewAgritur_QNAME, UserViewAgritur.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAgriturByPlaceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.businesslayer/", name = "getAgriturByPlaceResponse")
    public JAXBElement<GetAgriturByPlaceResponse> createGetAgriturByPlaceResponse(GetAgriturByPlaceResponse value) {
        return new JAXBElement<GetAgriturByPlaceResponse>(_GetAgriturByPlaceResponse_QNAME, GetAgriturByPlaceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserMarkAgritur }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.businesslayer/", name = "userMarkAgritur")
    public JAXBElement<UserMarkAgritur> createUserMarkAgritur(UserMarkAgritur value) {
        return new JAXBElement<UserMarkAgritur>(_UserMarkAgritur_QNAME, UserMarkAgritur.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAgriturByQuery }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.businesslayer/", name = "getAgriturByQuery")
    public JAXBElement<GetAgriturByQuery> createGetAgriturByQuery(GetAgriturByQuery value) {
        return new JAXBElement<GetAgriturByQuery>(_GetAgriturByQuery_QNAME, GetAgriturByQuery.class, null, value);
    }

}
