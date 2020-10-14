package fr.mnhn.diversity.indicator.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Map;

import fr.mnhn.diversity.indicator.IndicatorValue;
import fr.mnhn.diversity.territory.Territory;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Tests for {@link IndicatorService}
 * @author JB Nizet
 */
@RestClientTest
@Import(IndicatorConfig.class)
class IndicatorServiceTest {

    @Autowired
    @Indicators
    private WebClient webClient;

    private IndicatorService service;
    private MockWebServer server;

    @BeforeEach
    void prepare() throws IOException {
        server = new MockWebServer();
        server.start();

        WebClient mockWebClient = webClient.mutate().baseUrl(server.url("").toString()).build();
        service = new IndicatorService(mockWebClient);
    }

    @AfterEach
    void cleanup() throws IOException {
        server.shutdown();
    }

    @Test
    void shouldGetIndicatorData() throws InterruptedException {
        String body =
            // language=json
            "{\n" +
                "  \"code\": \"SNB-TMF-16-TBO1\",\n" +
                "  \"label\": \"Taux de boisement dans les Outre-Mer\",\n" +
                "  \"shortLabel\": \"Évolution du taux de boisement dans les Outre-Mer\",\n" +
                "  \"description\": \"L'indicateur fournit la part des territoires ultramarins couverts par la forêt. Sont considérées comme forêts « des terres occupant une superficie de plus de 0,5 hectare avec des arbres atteignant une hauteur supérieure à cinq mètres et un couvert arboré de plus de 10 %, ou avec des arbres capables d’atteindre ces seuils in situ ; la définition exclut les terres à vocation agricole ou urbaine prédominante » (définition de l’organisation des Nations unies pour l’alimentation et l’agriculture - FAO). Les taux de boisement sont donnés globalement et individuellement, pour 11 territoires ultramarins, d’après les inventaires successifs de l'Evaluation des ressources forestières mondiales ou Forest Ressource Assessement (FRA) de la FAO de 1990, 1995, 2000, 2005,2010 et 2015.\",\n" +
                "  \"catcherPhrase\": \"La forêt occupe 85 % du territoire en Outre-mer.\",\n" +
                "  \"creationDate\": \"2020-10-07\",\n" +
                "  \"status\": \"En cours\",\n" +
                "  \"operator\": \"Non renseigné\",\n" +
                "  \"backer\": \"ONB\",\n" +
                "  \"interest\": \"\",\n" +
                "  \"impactLevel\": \"\",\n" +
                "  \"sets\": [\n" +
                "    \"Biodiversité & forêt\",\n" +
                "    \"Biodiversité & outre-mer\"\n" +
                "  ],\n" +
                "  \"associatedQuestion\": [\n" +
                "    \"Quels sont l'état et la dynamique de la biodiversité en forêt ?\",\n" +
                "    \"Comment les facteurs qui influencent l’état de la biodiversité en forêt évoluent-ils ?\"\n" +
                "  ],\n" +
                "  \"interpretationKey\": \"Les forêts primaires et autres écosystèmes forestiers des  territoires Outre-mer français présentent une biodiversité considérable. La forêt tropicale humide guyanaise présente une biodiversité extrêment riche et complexe, l'une des plus importante au monde, que l'on ne connait encore que très partiellement. En outre, les îles ultramarines abritent un grand nombre d'espèces endémiques. Les enjeux de conservation de la biodiversité forestière y sont également importants. Le destruction des forêts est reconnue comme une pression majeure pesant sur la biodiversité forestière. En effet, une diminution de la quantité d'habitats disponibles entraine généralement une diminution de la richesse en espèces au sein de ces habitats. Le suivi du taux de boisement dans les Outre-Mer français donne une information à la fois sur l'intensité d'une menace (la destruction des forêts) et, indirectement, sur l'état de la biodiversité forestière. L'indicateur englobe 11 territoires ultramarins très hétérogènes avec des caractéristiques forestières différentes. Les surfaces forestières ultramarines les plus importantes sont en Guyane (plus de 8 millions d’hectares, soit près de 99 % de la superficie de ce territoire) et en Nouvelle-Calédonie (plus de 800 000 hectares, soit un taux de boisement de 46 %). La forêt de Polynésie française arrive en troisième position avec 155 000 hectares (taux de boisement de 42 %) tandis que trois autres territoires insulaires (Guadeloupe, Martinique, Réunion) présentent des superficies forestières plus faibles  (50-100 000 ha) et des taux de boisement variant entre 35 et 45 %. Les petites îles de Saint-Martin (Mer des Caraïbes), Mayotte (Océan Indien) et Saint-Pierre-et-Miquelon (océan Atlantique Nord) sont les plus faiblement boisées (< 20 %)  et Saint-Barthélemy (Mer des Caraïbes) est dépourvue de toute végétation forestière. Le taux de boisement global des territoires Outre-Mer considérés, retracé par les inventaires de la FAO (Forest ressources assessment ou FRA) depuis 1990, reste stable  (85 %). Plus en détail, les évolutions des taux de boisement des territoires ultramarins depuis 25 ans sont également de faibles amplitudes : - la forêt amazonienne guyanaise recule très faiblement (environ 3500 ha/an, hors création du barrage du Petit Saut) ; - les autres forêts ultramarines d’une certaine importance en superficie (Guadeloupe, Martinique, Réunion, Nouvelle-Calédonie) semblent connaître peu d’évolution, mais la fiabilité des estimations est souvent assez faible ; - la baisse apparente de la forêt de Mayotte  est pour partie au moins d’ordre méthodologique, mais la pression exercée par une population en hausse, due à une forte immigration, pourrait jouer un rôle ; - les raisons de la hausse apparente du taux de boisement en Polynésie sont également incertaines, et pour partie d’ordre méthodologique (en l’absence d’inventaire forestier).\",\n" +
                "  \"associatedIndicators\": [\n" +
                "    \"\"\n" +
                "  ],\n" +
                "  \"areas\": [\n" +
                "    \"Forêts\"\n" +
                "  ],\n" +
                "  \"pressures\": [\n" +
                "    \"Destruction des habitats\"\n" +
                "  ],\n" +
                "  \"policies\": [\n" +
                "    \"Gestion des espaces naturels\"\n" +
                "  ],\n" +
                "  \"nationalGoal\": [\n" +
                "    \"Préserver les espèces et leur diversité\",\n" +
                "    \"Préserver et restaurer les écosystèmes et leur fonctionnement\",\n" +
                "    \"Maîtriser les pressions sur la biodiversité\"\n" +
                "  ],\n" +
                "  \"europeanGoal\": [\n" +
                "    \"\"\n" +
                "  ],\n" +
                "  \"internationalGoal\": [],\n" +
                "  \"landUseType\": [\n" +
                "    \"\"\n" +
                "  ],\n" +
                "  \"relationGoal\": [\n" +
                "    \"En écologie, certaines théories montrent que la richesse spécifique d'un habitat est proportionnelle à sa taille. Le corollaire de cette approche est que la réduction des habitats conduit inéluctablement à la réduction de la biodiversité globale au sein de ces habitats, selon une loi exponentielle dite \\\"Loi d'Arrhenius\\\"(Chevassus-Au-Louis, 2006). Ainsi, la destruction des habitats est reconnue comme étant une des pressions majeures pesant sur la biodiversité au niveau mondial. Une diminution des taux de boisement dans les territoires Outre-Mer français devraient donc poser des problèmes à une partie de la biodiversité forestière de ces territoires.\"\n" +
                "  ],\n" +
                "  \"updatingFrequency\": \"2 à 5 ans\",\n" +
                "  \"breakSerie\": [\n" +
                "    \"Non\"\n" +
                "  ],\n" +
                "  \"remarkBreakSerie\": \"\",\n" +
                "  \"geographicalCoverage\": [\n" +
                "    \"Outre-mer\"\n" +
                "  ],\n" +
                "  \"dataOrigin\": \"Les données sont publiées tous les 5 ans depuis 1990 dans le cadre de l'Evaluation des ressources mondiales (Forest Ressources Assessment ou FRA) de la FAO. Le rapportage pour le FRA est organisé par territoire ultramarin. Si le rapporteur est souvent l’ONF, les producteurs des données sont variables :  IFN (IGN), Consultants, etc. 11 Territoires ultramarins français : - DROM (Guyane, Guadeloupe, Martinique, La Réunion, Mayotte) ; - COM (Saint-Martin, Saint-Barthélemy, Nouvelle-Calédonie, Polynésie française, Wallis-et-Futuna, Saint-Pierre-et-Miquelon) ; - Les Terres australes et antarctiques françaises (TAAF) sont exclues car elles ne portent pas de végétation forestière. Les années de rendu statistique (1990, 1995, 2000, 2005, 2010, 2015) cachent des dates très variables de prise de données. On peut citer à titre d’exemples : - Guyane : 1989, 1990, 1993, 2000, 2006, 2010 (principalement des données satellitales évaluées par l'IFN en 1990 et 2006) ; - Réunion :  1990, 2000, 2005, 2010, 2015 ; - Guadeloupe  1988, 1997, 2004, 2010/11/12/13, 2015 ; - Martinique : 1997, 2006, 2008, 2012, 2010 2013 ; - Mayotte :  1949, 1997, 2003, 2010, 2012.\",\n" +
                "  \"qualityRobustnessOpinion\": [\n" +
                "    \"Robuste\"\n" +
                "  ],\n" +
                "  \"qualityPrecision\": [\n" +
                "    \"Précis\"\n" +
                "  ],\n" +
                "  \"qualitySensitivity\": [\n" +
                "    \"\"\n" +
                "  ],\n" +
                "  \"qualityEfficiency\": [\n" +
                "    \"\"\n" +
                "  ],\n" +
                "  \"qualityDataAccessibility\": [\n" +
                "    \"Facilement accessibles\"\n" +
                "  ],\n" +
                "  \"qualityDataHomogeneity\": [\n" +
                "    \"Assez homogènes\"\n" +
                "  ],\n" +
                "  \"qualityDataReliability\": [\n" +
                "    \"Assez fiables\"\n" +
                "  ],\n" +
                "  \"qualityDataContinuity\": [\n" +
                "    \"Pérenne\"\n" +
                "  ],\n" +
                "  \"qualityDataAbundance\": [\n" +
                "    \"Assez abondantes\"\n" +
                "  ],\n" +
                "  \"qualityDataCost\": [\n" +
                "    \"Coût élevé\"\n" +
                "  ],\n" +
                "  \"qualityLevelAppropriation\": [\n" +
                "    \"Familier\"\n" +
                "  ],\n" +
                "  \"qualityRemarks\": \"\",\n" +
                "  \"advantages\": \"\",\n" +
                "  \"limits\": \"\",\n" +
                "  \"improvements\": \"L’amélioration de la qualité des résultats sera graduelle, fonction de l'évolution des méthodes de suivi. En 2015, le ministère en charge de l’environnement a annoncé un projet visant à établir ou améliorer le suivi cartographique des territoires ultramarins (espaces naturels). A moyen terme, il devrait être possible de disposer de données de surface pour les principaux types forestiers dans une partie au moins des territoires ultramarins.\",\n" +
                "  \"calculReference\": \"6cd63757-73d1-4001-95ff-102951b0ed47\",\n" +
                "  \"publiONB\": true,\n" +
                "  \"publiINPN\": false,\n" +
                "  \"publiSIE\": false,\n" +
                "  \"publiBIOM\": false,\n" +
                "  \"tags\": [\n" +
                "    \"\"\n" +
                "  ],\n" +
                "  \"resultDescription\": \"\",\n" +
                "  \"version\": 1,\n" +
                "  \"targetValue\": \"Non\",\n" +
                "  \"concernedOrientation\": [\n" +
                "    \"B - Préserver le vivant et sa capacité à évoluer\",\n" +
                "    \"D - Assurer un usage durable et équitable de la biodiversité\"\n" +
                "  ],\n" +
                "  \"_links\": {\n" +
                "    \"self\": {\n" +
                "      \"href\": \"http://odata-indicateurs.mnhn.fr/indicators/8816092b-1ce3-4ae7-815d-019e99ecf545\"\n" +
                "    },\n" +
                "    \"calculationReference\": [\n" +
                "      {\n" +
                "        \"href\": \"http://odata-indicateurs.mnhn.fr/calculations/6cd63757-73d1-4001-95ff-102951b0ed47{?embed}\",\n" +
                "        \"templated\": true\n" +
                "      },\n" +
                "      {\n" +
                "        \"href\": \"http://odata-indicateurs.mnhn.fr/calculations/6cd63757-73d1-4001-95ff-102951b0ed47{?embed}\",\n" +
                "        \"templated\": true\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        server.enqueue(
            new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(body)
        );

        String indicatorId = "8816092b-1ce3-4ae7-815d-019e99ecf545";
        IndicatorData indicator = service.indicatorData(indicatorId).block();

        assertThat(indicator).isEqualTo(
            new IndicatorData(
                indicatorId,
                "Évolution du taux de boisement dans les Outre-Mer",
                "6cd63757-73d1-4001-95ff-102951b0ed47"
            )
        );

        assertThat(server.takeRequest().getPath()).isEqualTo("/indicators/" + indicatorId);
    }

    @Test
    void  shouldGetIndicatorValue() throws InterruptedException {
        String body =
            // language=json
            "{\n" +
                "  \"indicatorId\": \"8816092b-1ce3-4ae7-815d-019e99ecf545\",\n" +
                "  \"title\": \"Taux de boisement dans les Outre-Mer\",\n" +
                "  \"date\": \"2016-05-19\",\n" +
                "  \"vintage\": \"2015\",\n" +
                "  \"analysisDate\": \"2018-07-23\",\n" +
                "  \"comment\": \"Cet indicateur a fait l'objet d'une analyse en 2018, consultable ci-dessous, qui fait actuellement l'objet d'une expertise pour faire évoluer la fiche Indicateur si nécessaire.\",\n" +
                "  \"_links\": {\n" +
                "    \"self\": {\n" +
                "      \"href\": \"http://odata-indicateurs.mnhn.fr/calculations/6cd63757-73d1-4001-95ff-102951b0ed47\"\n" +
                "    },\n" +
                "    \"resource\": {\n" +
                "      \"href\": \"http://odata-indicateurs.mnhn.fr/calculations/6cd63757-73d1-4001-95ff-102951b0ed47/resource\"\n" +
                "    },\n" +
                "    \"resourceAnalyze\": {\n" +
                "      \"href\": \"http://odata-indicateurs.mnhn.fr/calculations/6cd63757-73d1-4001-95ff-102951b0ed47/resourceAnalyze\"\n" +
                "    },\n" +
                "    \"indicator\": {\n" +
                "      \"href\": \"http://odata-indicateurs.mnhn.fr/indicators/8816092b-1ce3-4ae7-815d-019e99ecf545\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"_embedded\": {\n" +
                "    \"calculationResults\": [\n" +
                "      {\n" +
                "        \"code\": \"R1\",\n" +
                "        \"calculationId\": \"6cd63757-73d1-4001-95ff-102951b0ed47\",\n" +
                "        \"title\": \"Valeur de l'indicateur\",\n" +
                "        \"main\": true,\n" +
                "        \"description\": \"Pourcentage en 2015\",\n" +
                "        \"source\": \"Rapportage FRA (Forest Ressources Assessment)\",\n" +
                "        \"note\": \"\",\n" +
                "        \"resourceFilename\": \"\",\n" +
                "        \"resourceFormat\": \"\",\n" +
                "        \"resourceType\": \"\",\n" +
                "        \"resourceTitle\": \"\",\n" +
                "        \"resourceSource\": \"\",\n" +
                "        \"resourcesPath\": null,\n" +
                "        \"values\": [\n" +
                "          {\n" +
                "            \"hold\": \"Outre-mer\",\n" +
                "            \"period\": \"2015\",\n" +
                "            \"thematic\": \"Taux de boisement\",\n" +
                "            \"metric\": [\n" +
                "              {\n" +
                "                \"label\": \"\",\n" +
                "                \"value\": \"84,64046368\",\n" +
                "                \"unit\": \"%\"\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        \"declination\": \"\",\n" +
                "        \"identifiant\": \"a236a792-e145-4bb1-9bc5-14ae09ff1373\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"code\": \"R2\",\n" +
                "        \"calculationId\": \"6cd63757-73d1-4001-95ff-102951b0ed47\",\n" +
                "        \"title\": \"Évolution de la surface forestière et du taux de boisement par territoire\",\n" +
                "        \"main\": false,\n" +
                "        \"description\": \"\",\n" +
                "        \"source\": \"Rapportage FRA (Forest Ressources Assessment)\",\n" +
                "        \"note\": \"\",\n" +
                "        \"resourceFilename\": \"\",\n" +
                "        \"resourceFormat\": \"\",\n" +
                "        \"resourceType\": \"\",\n" +
                "        \"resourceTitle\": \"\",\n" +
                "        \"resourceSource\": \"\",\n" +
                "        \"resourcesPath\": null,\n" +
                "        \"values\": [\n" +
                "          {\n" +
                "            \"hold\": \"Nouvelle-Calédonie\",\n" +
                "            \"period\": \"2015\",\n" +
                "            \"thematic\": \"Taux de boisement\",\n" +
                "            \"metric\": [\n" +
                "              {\n" +
                "                \"label\": \"\",\n" +
                "                \"value\": \"45,78774617\",\n" +
                "                \"unit\": \"%\"\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"hold\": \"Guadeloupe\",\n" +
                "            \"period\": \"2015\",\n" +
                "            \"thematic\": \"Taux de boisement\",\n" +
                "            \"metric\": [\n" +
                "              {\n" +
                "                \"label\": \"\",\n" +
                "                \"value\": \"42,26190476\",\n" +
                "                \"unit\": \"%\"\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"hold\": \"La Réunion\",\n" +
                "            \"period\": \"2015\",\n" +
                "            \"thematic\": \"Taux de boisement\",\n" +
                "            \"metric\": [\n" +
                "              {\n" +
                "                \"label\": \"\",\n" +
                "                \"value\": \"35,2\",\n" +
                "                \"unit\": \"%\"\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"hold\": \"Martinique\",\n" +
                "            \"period\": \"2015\",\n" +
                "            \"thematic\": \"Taux de boisement\",\n" +
                "            \"metric\": [\n" +
                "              {\n" +
                "                \"label\": \"\",\n" +
                "                \"value\": \"46,22641509\",\n" +
                "                \"unit\": \"%\"\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"hold\": \"Saint-Pierre-et-Miquelon\",\n" +
                "            \"period\": \"2015\",\n" +
                "            \"thematic\": \"Taux de boisement\",\n" +
                "            \"metric\": [\n" +
                "              {\n" +
                "                \"label\": \"\",\n" +
                "                \"value\": \"13,04347826\",\n" +
                "                \"unit\": \"%\"\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        \"declination\": \"Territoires\",\n" +
                "        \"identifiant\": \"115b8c99-feaf-4c2c-9de3-f674e70a3ee3\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        server.enqueue(
            new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(body)
        );

        Map<Territory, IndicatorValue> indicatorValues = service.indicatorValues("6cd63757-73d1-4001-95ff-102951b0ed47").block();

        assertThat(indicatorValues.get(Territory.OUTRE_MER)).isEqualTo(
            new IndicatorValue(
                84.64046368,
                "%"
            )
        );

        assertThat(indicatorValues.get(Territory.GUADELOUPE)).isEqualTo(
            new IndicatorValue(
                42.26190476,
                "%"
            )
        );
        assertThat(indicatorValues.get(Territory.GUYANE)).isNull();

        assertThat(server.takeRequest().getPath()).isEqualTo("/calculations/6cd63757-73d1-4001-95ff-102951b0ed47?embed=CALCULATIONRESULTS");
    }
}
