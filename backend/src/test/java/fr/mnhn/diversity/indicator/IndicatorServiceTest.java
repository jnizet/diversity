package fr.mnhn.diversity.indicator;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

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
 * Tests for {@link fr.mnhn.diversity.indicator.IndicatorService}
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
                "    \"code\": \"SNB-TMF-16-UCA1\",\n" +
                "    \"label\": \"Conservation du patrimoine génétique des arbres en forêt\",\n" +
                "    \"shortLabel\": \"Évolution du nombre d'unités conservatoires (UC) des ressources génétiques des principales essences forestières métropolitaines\",\n" +
                "    \"description\": \"Cet indicateur suit l’effort de conservation dynamique des ressources génétiques de populations autochtones d’arbres forestiers représentatives de la diversité génétique de l’espèce en France métropolitaine.\",\n" +
                "    \"catcherPhrase\": \"La conservation des ressources génétiques des arbres forestiers cible 9 espèces d’arbres et repose sur 101 unités de conservation in situ\",\n" +
                "    \"creationDate\": \"2019-11-20\",\n" +
                "    \"status\": \"En cours\",\n" +
                "    \"operator\": \"Irstea, UR EFNO, Nogent-sur-Vernisson\",\n" +
                "    \"backer\": \"ONB\",\n" +
                "    \"interest\": \"\",\n" +
                "    \"impactLevel\": \"\",\n" +
                "    \"sets\": [\n" +
                "        \"Biodiversité & forêt\"\n" +
                "    ],\n" +
                "    \"associatedQuestion\": [\n" +
                "        \"Quelles sont les actions et politiques mises en place pour préserver la biodiversité en forêt ?\"\n" +
                "    ],\n" +
                "    \"interpretationKey\": \"A la suite de la 1ere Conférence ministérielle pour la protection des forêts en Europe (Strasbourg, 1990), la France s’est engagée à mettre en œuvre une politique de conservation des ressources génétiques forestières. Comme préconisé par la Résolution 2 de la Conférence, la priorité a été donnée à la conservation in situ (en forêt) des ressources génétiques forestières. La Commission des Ressources Génétiques Forestières (CRGF) a défini les modalités pratiques de mise en œuvre de cette politique, notamment la mise en place d’un réseau national de gestion et de conservation des ressources génétiques des principales espèces d’arbres forestiers (cette approche est complétée par la conservation ex situ et des collections). La conservation in situ de ressources génétiques forestières d’intérêt national porte actuellement sur neuf espèces d’arbres ou essences : chêne sessile, épicéa commun, hêtre, orme de montagne, orme lisse, peuplier noir, pin maritime, pin sylvestre, sapin pectiné. Cela représente 8,4 % des essences forestières indigènes qui font ainsi l'objet d'un programme de conservation de leurs ressources génétiques. Les unités conservatoires (UC) visent la conservation dynamique des ressources génétiques autochtones représentatives de la diversité de l’espèce en France métropolitaine. Pour les essences dites « sociales » (qui tolèrent la concurrence intra-spécifique et peuvent constituer naturellement des peuplements purs de grande surface), une UC est typiquement constituée par (i) un noyau de 15 ha environ, enjeu principal des efforts de conservation, et (ii) d’une zone tampon de l’ordre d’une centaine d’hectares, dont le rôle essentiel est de limiter les flux de gènes extérieurs pouvant agir comme des sources d’altérations génétiques indésirables. Les modalités de gestion sont consignées dans une charte de gestion. Ces unités sont conçues et gérées de manière à ce que la régénération naturelle puisse se dérouler dans de bonnes conditions de brassage des gènes (nombreux parents, régénération abondante) et en faisant jouer la sélection naturelle pour faciliter le processus d'adaptation de la population aux changements de son environnement. Dans le cas des grandes essences sociales et d'UC situées au cœur de l'aire de l'espèce concernée, ces unités sont constituées de plusieurs milliers d’arbres reproducteurs. Dans le cas d'espèces plus rares ou de populations marginales, certaines UC peuvent comporter moins de cent arbres reproducteurs. Les critères de sélection des unités conservatoires portent sur l’autochtonie, l’isolement de sources de pollen et de graines exogènes, la taille de la population reproductrice et l’accord formel du propriétaire pour une gestion dynamique de la régénération naturelle.\",\n" +
                "    \"associatedIndicators\": [\n" +
                "        \"\"\n" +
                "    ],\n" +
                "    \"areas\": [\n" +
                "        \"Forêts\"\n" +
                "    ],\n" +
                "    \"pressures\": [\n" +
                "        \"\"\n" +
                "    ],\n" +
                "    \"policies\": [\n" +
                "        \"Gestion des espaces naturels\",\n" +
                "        \"Maîtrise des pressions liées aux activités humaines\",\n" +
                "        \"Action internationale et climatique\"\n" +
                "    ],\n" +
                "    \"nationalGoal\": [\n" +
                "        \"Préserver les espèces et leur diversité\",\n" +
                "        \"Préserver et restaurer les écosystèmes et leur fonctionnement\",\n" +
                "        \"Garantir la durabilité de l’utilisation des ressources biologiques\"\n" +
                "    ],\n" +
                "    \"europeanGoal\": [\n" +
                "        \"\"\n" +
                "    ],\n" +
                "    \"internationalGoal\": [],\n" +
                "    \"landUseType\": [\n" +
                "        \"\"\n" +
                "    ],\n" +
                "    \"relationGoal\": [\n" +
                "        \"La représentativité des réseaux est fondée sur un échantillonnage de la diversité des contextes bioclimatiques de présence de l’espèce et, dans la mesure du possible, sur les résultats d’études génétiques portant sur la structuration géographique de la diversité génétique actuelle (présence/absence de certains marqueurs moléculaires ou enzymatiques dans certaines populations) ou ancienne (ex : marqueurs de l’ADN chloroplastiques indicateurs de lignées issues de différents refuges glaciaires).\"\n" +
                "    ],\n" +
                "    \"updatingFrequency\": \"Annuelle\",\n" +
                "    \"breakSerie\": [\n" +
                "        \"Non\"\n" +
                "    ],\n" +
                "    \"remarkBreakSerie\": \"\",\n" +
                "    \"geographicalCoverage\": [\n" +
                "        \"Métropole\"\n" +
                "    ],\n" +
                "    \"dataOrigin\": \"Registre national des Matériels de base (tenu à jour deux fois par an par l'Irstea). Listes d’UC à télécharger ici http://agriculture.gouv.fr/la-politique-nationale-de-conservation-des-ressources-genetiques-forestieres\",\n" +
                "    \"qualityRobustnessOpinion\": [\n" +
                "        \"Très robuste\"\n" +
                "    ],\n" +
                "    \"qualityPrecision\": [\n" +
                "        \"Précis\"\n" +
                "    ],\n" +
                "    \"qualitySensitivity\": [\n" +
                "        \"Sensible\"\n" +
                "    ],\n" +
                "    \"qualityEfficiency\": [\n" +
                "        \"Efficace\"\n" +
                "    ],\n" +
                "    \"qualityDataAccessibility\": [\n" +
                "        \"Facilement accessibles\"\n" +
                "    ],\n" +
                "    \"qualityDataHomogeneity\": [\n" +
                "        \"Homogènes\"\n" +
                "    ],\n" +
                "    \"qualityDataReliability\": [\n" +
                "        \"Très fiables\"\n" +
                "    ],\n" +
                "    \"qualityDataContinuity\": [\n" +
                "        \"Pérennité garantie\"\n" +
                "    ],\n" +
                "    \"qualityDataAbundance\": [\n" +
                "        \"Abondantes\"\n" +
                "    ],\n" +
                "    \"qualityDataCost\": [\n" +
                "        \"Coût moyen\"\n" +
                "    ],\n" +
                "    \"qualityLevelAppropriation\": [\n" +
                "        \"Familier\"\n" +
                "    ],\n" +
                "    \"qualityRemarks\": \"\",\n" +
                "    \"advantages\": \"\",\n" +
                "    \"limits\": \"L’évolution du nombre d’UC sera comprise très aisément mais le concept de conservation dynamique n’est pas familier, voire contre-intuitif dans la mesure où l’on vise à conserver un processus d’adaptation et non à figer l’état d’une ressource.\",\n" +
                "    \"improvements\": \"Cet indicateur « de réponse » renseigne sur les mesures prises par le ministère en charge de la forêt pour favoriser la préservation de la biodiversité intraspécifique des espèces d’arbres des forêts métropolitaines. La CRGF souhaiterait le compléter par un « indicateur d’état » décrivant l’état et l’évolution de la diversité génétique de populations d’arbres forestiers mais, à l’heure actuelle, aucun indicateur standardisé n’est encore disponible pour ce genre d’évaluation sur le terrain.\",\n" +
                "    \"calculReference\": \"2d019839-0b90-422d-93f6-516db2d0fcc0\",\n" +
                "    \"publiONB\": true,\n" +
                "    \"publiINPN\": false,\n" +
                "    \"publiSIE\": false,\n" +
                "    \"tags\": [\n" +
                "        \"\"\n" +
                "    ],\n" +
                "    \"resultDescription\": \"\",\n" +
                "    \"version\": 1,\n" +
                "    \"targetValue\": \"Oui. En l'état actuel des connaissances de la structuration de la diversité génétique des essences forestières, il n'est pas possible de se référer à un nombre idéal d'unités conservatoires (UC) par essence pour une conservation efficace du pool génétique. Il n'y a donc pas de valeur cible à proprement parler, mais l’objectif de la CRGF est de l’ordre d’une vingtaine d’UC par grande essence sociale. EUFORGEN recommande aux pays participant au programme paneuropéen de sélectionner au moins une UC dans chacune des grandes zones bioclimatiques où l’espèce est indigène, ce nombre n’étant jamais supérieur à 5 en France (de Vries et al., 2015).\",\n" +
                "    \"concernedOrientation\": [\n" +
                "        \"B - Préserver le vivant et sa capacité à évoluer\",\n" +
                "        \"D - Assurer un usage durable et équitable de la biodiversité\"\n" +
                "    ],\n" +
                "    \"_links\": {\n" +
                "        \"self\": {\n" +
                "            \"href\": \"http://odata-indicateurs.mnhn.fr/indicators/c4201cd7-bd98-45a2-b8be-922fa2ad3cca\"\n" +
                "        },\n" +
                "        \"calculationReference\": [\n" +
                "            {\n" +
                "                \"href\": \"http://odata-indicateurs.mnhn.fr/calculations/2d019839-0b90-422d-93f6-516db2d0fcc0{?embed}\",\n" +
                "                \"templated\": true\n" +
                "            },\n" +
                "            {\n" +
                "                \"href\": \"http://odata-indicateurs.mnhn.fr/calculations/2d019839-0b90-422d-93f6-516db2d0fcc0{?embed}\",\n" +
                "                \"templated\": true\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";

        server.enqueue(
            new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(body)
        );

        String indicatorId = "c4201cd7-bd98-45a2-b8be-922fa2ad3cca";
        IndicatorData indicator = service.indicatorData(indicatorId).block();

        assertThat(indicator).isEqualTo(
            new IndicatorData(
                indicatorId,
                "Évolution du nombre d'unités conservatoires (UC) des ressources génétiques des principales essences forestières métropolitaines",
                "2d019839-0b90-422d-93f6-516db2d0fcc0"
            )
        );

        assertThat(server.takeRequest().getPath()).isEqualTo("/indicators/" + indicatorId);
    }

    @Test
    void  shouldGetIndicatorValue() throws InterruptedException {
        String body =
            // language=json
            "{\n" +
                "    \"indicatorId\": \"a75b794e-826f-448b-a695-45bb8b0245e1\",\n" +
                "    \"title\": \"Aires protégées terrestres en métropole\",\n" +
                "    \"date\": \"2018-12-14\",\n" +
                "    \"vintage\": \"2018\",\n" +
                "    \"analysisDate\": null,\n" +
                "    \"comment\": \"REMARQUE : cet indicateur a fait l'objet d'une deuxième analyse en 2015-2016, consultable ci-dessous, qui fait actuellement l'objet d'une expertise pour faire évoluer la fiche Indicateur si nécessaire. Une appréciation du degré de prise en considération des remarques émises lors de cette deuxième évaluation  sera publiée  ci-dessous à l'issue.\",\n" +
                "    \"_links\": {\n" +
                "        \"self\": {\n" +
                "            \"href\": \"http://odata-indicateurs.mnhn.fr/calculations/1efa9368-2a31-4415-91e3-d5cc2f224b7a\"\n" +
                "        },\n" +
                "        \"resource\": {\n" +
                "            \"href\": \"http://odata-indicateurs.mnhn.fr/calculations/1efa9368-2a31-4415-91e3-d5cc2f224b7a/resource\"\n" +
                "        },\n" +
                "        \"indicator\": {\n" +
                "            \"href\": \"http://odata-indicateurs.mnhn.fr/indicators/a75b794e-826f-448b-a695-45bb8b0245e1\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"_embedded\": {\n" +
                "        \"calculationResults\": [\n" +
                "            {\n" +
                "                \"code\": \"R3\",\n" +
                "                \"calculationId\": \"1efa9368-2a31-4415-91e3-d5cc2f224b7a\",\n" +
                "                \"title\": \"Historique de l'indicateur\",\n" +
                "                \"main\": false,\n" +
                "                \"description\": \"\",\n" +
                "                \"source\": \"INPN, UMS PatriNat (AFB-CNRS-MNHN) - Base Espaces protégés, printemps 2018. Traitements : SDES, 2018\",\n" +
                "                \"note\": \"\",\n" +
                "                \"resourceFilename\": \"visuel2.png\",\n" +
                "                \"resourceFormat\": \"image\",\n" +
                "                \"resourceType\": \"diagramme\",\n" +
                "                \"resourceTitle\": \"Évolution de la part de la superficie terrestre du territoire métropolitain classée en aires protégées (protection forte)\",\n" +
                "                \"resourceSource\": \"INPN, UMS PatriNat (AFB-CNRS-MNHN) - Base Espaces protégés, printemps 2018. Traitements : SDES, 2018\",\n" +
                "                \"resourcesPath\": \"a75b794e-826f-448b-a695-45bb8b0245e1/1efa9368-2a31-4415-91e3-d5cc2f224b7a/visuel2.png\",\n" +
                "                \"values\": [\n" +
                "                    {\n" +
                "                        \"hold\": \"\",\n" +
                "                        \"period\": \"2017\",\n" +
                "                        \"thematic\": \"Valeur non publiée\",\n" +
                "                        \"metric\": [\n" +
                "                            {\n" +
                "                                \"label\": \"En mars 2017\",\n" +
                "                                \"value\": \"1,352370238\",\n" +
                "                                \"unit\": \"%\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"hold\": \"\",\n" +
                "                        \"period\": \"2015\",\n" +
                "                        \"thematic\": \"Valeur publiée en mai 2015\",\n" +
                "                        \"metric\": [\n" +
                "                            {\n" +
                "                                \"label\": \"En février 2015\",\n" +
                "                                \"value\": \"1,345064269\",\n" +
                "                                \"unit\": \"%\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"hold\": \"\",\n" +
                "                        \"period\": \"2016\",\n" +
                "                        \"thematic\": \"Valeur publiée en mai 2016\",\n" +
                "                        \"metric\": [\n" +
                "                            {\n" +
                "                                \"label\": \"En février 2016\",\n" +
                "                                \"value\": \"1,350429485\",\n" +
                "                                \"unit\": \"%\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"hold\": \"\",\n" +
                "                        \"period\": \"2011\",\n" +
                "                        \"thematic\": \"Valeur publiée en mai 2012\",\n" +
                "                        \"metric\": [\n" +
                "                            {\n" +
                "                                \"label\": \"Au 1er janvier 2011\",\n" +
                "                                \"value\": \"1,265416055\",\n" +
                "                                \"unit\": \"%\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"hold\": \"\",\n" +
                "                        \"period\": \"2012\",\n" +
                "                        \"thematic\": \"Valeur publiée en mai 2013\",\n" +
                "                        \"metric\": [\n" +
                "                            {\n" +
                "                                \"label\": \"Au 1er janvier 2012\",\n" +
                "                                \"value\": \"1,280404859\",\n" +
                "                                \"unit\": \"%\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"hold\": \"\",\n" +
                "                        \"period\": \"2013\",\n" +
                "                        \"thematic\": \"Valeur publiée en mai 2014\",\n" +
                "                        \"metric\": [\n" +
                "                            {\n" +
                "                                \"label\": \"Au 1er juin 2013\",\n" +
                "                                \"value\": \"1,315002972\",\n" +
                "                                \"unit\": \"%\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"hold\": \"\",\n" +
                "                        \"period\": \"2018\",\n" +
                "                        \"thematic\": \"Valeur publiée en septembre 2018\",\n" +
                "                        \"metric\": [\n" +
                "                            {\n" +
                "                                \"label\": \"Au printemps 2018\",\n" +
                "                                \"value\": \"1,365192838\",\n" +
                "                                \"unit\": \"%\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"declination\": \"\",\n" +
                "                \"identifiant\": \"01484262-931e-470b-aaa1-6c66e4f69077\",\n" +
                "                \"_links\": {\n" +
                "                    \"resource\": {\n" +
                "                        \"href\": \"http://odata-indicateurs.mnhn.fr/calculationResults/01484262-931e-470b-aaa1-6c66e4f69077/resource\"\n" +
                "                    }\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                \"code\": \"R2\",\n" +
                "                \"calculationId\": \"1efa9368-2a31-4415-91e3-d5cc2f224b7a\",\n" +
                "                \"title\": \"Surface terrestre et proportion du territoire classée en aires protégées\",\n" +
                "                \"main\": false,\n" +
                "                \"description\": \"\",\n" +
                "                \"source\": \"INPN, UMS PatriNat (AFB-CNRS-MNHN) - Base Espaces protégés, printemps 2018. Traitements : SDES, 2018\",\n" +
                "                \"note\": \"\",\n" +
                "                \"resourceFilename\": \"visuel1.png\",\n" +
                "                \"resourceFormat\": \"image\",\n" +
                "                \"resourceType\": \"diagramme\",\n" +
                "                \"resourceTitle\": \"Surfaces terrestres des aires protégées métropolitaines (protections fortes)\",\n" +
                "                \"resourceSource\": \"INPN, UMS PatriNat (AFB-CNRS-MNHN) - Base Espaces protégés, printemps 2018. Traitements : SDES, 2018\",\n" +
                "                \"resourcesPath\": \"a75b794e-826f-448b-a695-45bb8b0245e1/1efa9368-2a31-4415-91e3-d5cc2f224b7a/visuel1.png\",\n" +
                "                \"values\": [\n" +
                "                    {\n" +
                "                        \"hold\": \"\",\n" +
                "                        \"period\": \"2018\",\n" +
                "                        \"thematic\": \"Arrêté de protection de biotope\",\n" +
                "                        \"metric\": [\n" +
                "                            {\n" +
                "                                \"label\": \"Surface terrestre\",\n" +
                "                                \"value\": \"1630,6584\",\n" +
                "                                \"unit\": \"km²\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"label\": \"Proportion du territoire\",\n" +
                "                                \"value\": \"0,296962956\",\n" +
                "                                \"unit\": \"%\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"hold\": \"\",\n" +
                "                        \"period\": \"2018\",\n" +
                "                        \"thematic\": \"Cœur de parc national\",\n" +
                "                        \"metric\": [\n" +
                "                            {\n" +
                "                                \"label\": \"Surface terrestre\",\n" +
                "                                \"value\": \"3632,8723\",\n" +
                "                                \"unit\": \"km²\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"label\": \"Proportion du territoire\",\n" +
                "                                \"value\": \"0,661590739\",\n" +
                "                                \"unit\": \"%\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"hold\": \"Métropole\",\n" +
                "                        \"period\": \"2018\",\n" +
                "                        \"thematic\": \"Protection forte (sans doubles comptes)\",\n" +
                "                        \"metric\": [\n" +
                "                            {\n" +
                "                                \"label\": \"Surface terrestre\",\n" +
                "                                \"value\": \"7496,4339\",\n" +
                "                                \"unit\": \"km²\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"hold\": \"\",\n" +
                "                        \"period\": \"2018\",\n" +
                "                        \"thematic\": \"Réserve biologique\",\n" +
                "                        \"metric\": [\n" +
                "                            {\n" +
                "                                \"label\": \"Surface terrestre\",\n" +
                "                                \"value\": \"426,0324\",\n" +
                "                                \"unit\": \"km²\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"label\": \"Proportion du territoire\",\n" +
                "                                \"value\": \"0,077585741\",\n" +
                "                                \"unit\": \"%\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"hold\": \"Métropole\",\n" +
                "                        \"period\": \"2018\",\n" +
                "                        \"thematic\": \"Territoire entier\",\n" +
                "                        \"metric\": [\n" +
                "                            {\n" +
                "                                \"label\": \"Surface terrestre\",\n" +
                "                                \"value\": \"549111,7219\",\n" +
                "                                \"unit\": \"km²\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"hold\": \"\",\n" +
                "                        \"period\": \"2018\",\n" +
                "                        \"thematic\": \"Réserve naturelle nationale\",\n" +
                "                        \"metric\": [\n" +
                "                            {\n" +
                "                                \"label\": \"Surface terrestre\",\n" +
                "                                \"value\": \"1524,0775\",\n" +
                "                                \"unit\": \"km²\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"label\": \"Proportion du territoire\",\n" +
                "                                \"value\": \"0,277553263\",\n" +
                "                                \"unit\": \"%\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"hold\": \"\",\n" +
                "                        \"period\": \"2018\",\n" +
                "                        \"thematic\": \"Réserve naturelle régionale\",\n" +
                "                        \"metric\": [\n" +
                "                            {\n" +
                "                                \"label\": \"Surface terrestre\",\n" +
                "                                \"value\": \"375,595\",\n" +
                "                                \"unit\": \"km²\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"label\": \"Proportion du territoire\",\n" +
                "                                \"value\": \"0,06840047\",\n" +
                "                                \"unit\": \"%\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"hold\": \"\",\n" +
                "                        \"period\": \"2018\",\n" +
                "                        \"thematic\": \"Réserve naturelle de Corse\",\n" +
                "                        \"metric\": [\n" +
                "                            {\n" +
                "                                \"label\": \"Surface terrestre\",\n" +
                "                                \"value\": \"32,1569\",\n" +
                "                                \"unit\": \"km²\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"label\": \"Proportion du territoire\",\n" +
                "                                \"value\": \"0,005856167\",\n" +
                "                                \"unit\": \"%\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"declination\": \"\",\n" +
                "                \"identifiant\": \"b8602dcf-ade7-400a-91b0-aef4e343898b\",\n" +
                "                \"_links\": {\n" +
                "                    \"resource\": {\n" +
                "                        \"href\": \"http://odata-indicateurs.mnhn.fr/calculationResults/b8602dcf-ade7-400a-91b0-aef4e343898b/resource\"\n" +
                "                    }\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                \"code\": \"R1\",\n" +
                "                \"calculationId\": \"1efa9368-2a31-4415-91e3-d5cc2f224b7a\",\n" +
                "                \"title\": \"Valeur de l'indicateur\",\n" +
                "                \"main\": true,\n" +
                "                \"description\": \"Pourcentage au printemps 2018\",\n" +
                "                \"source\": \"INPN, UMS PatriNat (AFB-CNRS-MNHN) - Base Espaces protégés, printemps 2018. Traitements : SDES, 2018\",\n" +
                "                \"note\": \"\",\n" +
                "                \"resourceFilename\": \"\",\n" +
                "                \"resourceFormat\": \"\",\n" +
                "                \"resourceType\": \"\",\n" +
                "                \"resourceTitle\": \"\",\n" +
                "                \"resourceSource\": \"\",\n" +
                "                \"resourcesPath\": null,\n" +
                "                \"values\": [\n" +
                "                    {\n" +
                "                        \"hold\": \"Métropole\",\n" +
                "                        \"period\": \"2018\",\n" +
                "                        \"thematic\": \"Protection forte (sans doubles comptes)\",\n" +
                "                        \"metric\": [\n" +
                "                            {\n" +
                "                                \"label\": \"Proportion du territoire\",\n" +
                "                                \"value\": \"1,365192838\",\n" +
                "                                \"unit\": \"%\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"declination\": \"\",\n" +
                "                \"identifiant\": \"dae463a9-60ba-4109-ac09-448d9036b27d\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";

        server.enqueue(
            new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(body)
        );

        IndicatorValue indicatorValue = service.indicatorValue("1efa9368-2a31-4415-91e3-d5cc2f224b7a").block();

        assertThat(indicatorValue).isEqualTo(
            new IndicatorValue(
                1.365192838,
                "%"
            )
        );

        assertThat(server.takeRequest().getPath()).isEqualTo("/calculations/1efa9368-2a31-4415-91e3-d5cc2f224b7a?embed=CALCULATIONRESULTS");
    }
}
