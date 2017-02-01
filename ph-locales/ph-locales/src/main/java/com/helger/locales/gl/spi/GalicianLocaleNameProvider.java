/**
 * Copyright (C) 2015-2017 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.locales.gl.spi;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.spi.LocaleNameProvider;

import com.helger.locales.gl.GalicianLocales;

public final class GalicianLocaleNameProvider extends LocaleNameProvider
{
  private static Map <String, String> _createDisplayCountries ()
  {
    final Map <String, String> aMap = new HashMap <String, String> (260);
    aMap.put ("AC", "Illa de Ascensi\u00f3n");
    aMap.put ("AD", "Andorra");
    aMap.put ("AE", "Emiratos \u00c1rabes Unidos");
    aMap.put ("AF", "Afganist\u00e1n");
    aMap.put ("AG", "Antiga e Barbuda");
    aMap.put ("AI", "Anguila");
    aMap.put ("AL", "Albania");
    aMap.put ("AM", "Armenia");
    aMap.put ("AN", "Antillas Holandesas");
    aMap.put ("AO", "Angola");
    aMap.put ("AQ", "Ant\u00e1rtida");
    aMap.put ("AR", "Arxentina");
    aMap.put ("AS", "Samoa Americana");
    aMap.put ("AT", "Austria");
    aMap.put ("AU", "Australia");
    aMap.put ("AW", "Aruba");
    aMap.put ("AX", "\u00c5land");
    aMap.put ("AZ", "Acerbaix\u00e1n");
    aMap.put ("BA", "Bosnia e Hercegovina");
    aMap.put ("BB", "Barbados");
    aMap.put ("BD", "Bangladesh");
    aMap.put ("BE", "B\u00e9lxica");
    aMap.put ("BF", "Burkina Faso");
    aMap.put ("BG", "Bulgaria");
    aMap.put ("BH", "Bahrein");
    aMap.put ("BI", "Burundi");
    aMap.put ("BJ", "Benin");
    aMap.put ("BL", "San Bartolom\u00e9");
    aMap.put ("BM", "Bermudas");
    aMap.put ("BN", "Brunei");
    aMap.put ("BO", "Bolivia");
    aMap.put ("BQ", "Caribe neerland\u00e9s");
    aMap.put ("BR", "Brasil");
    aMap.put ("BS", "Bahamas");
    aMap.put ("BT", "But\u00e1n");
    aMap.put ("BV", "Illa Bouvet");
    aMap.put ("BW", "Botsuana");
    aMap.put ("BY", "Bielorrusia");
    aMap.put ("BZ", "Belice");
    aMap.put ("CA", "Canad\u00e1");
    aMap.put ("CC", "Illas Cocos");
    aMap.put ("CD", "Rep\u00fablica Democr\u00e1tica do Congo");
    aMap.put ("CF", "Rep\u00fablica Centroafricana");
    aMap.put ("CG", "Congo");
    aMap.put ("CH", "Su\u00edza");
    aMap.put ("CI", "Costa de Marfil");
    aMap.put ("CK", "Illas Cook");
    aMap.put ("CL", "Chile");
    aMap.put ("CM", "Camer\u00fan");
    aMap.put ("CN", "China");
    aMap.put ("CO", "Colombia");
    aMap.put ("CP", "Illa Clipperton");
    aMap.put ("CR", "Costa Rica");
    aMap.put ("CU", "Cuba");
    aMap.put ("CV", "Cabo Verde");
    aMap.put ("CW", "Cura\u00e7ao");
    aMap.put ("CX", "Illa Christmas");
    aMap.put ("CY", "Chipre");
    aMap.put ("CZ", "Rep\u00fablica Checa");
    aMap.put ("DE", "Alema\u00f1a");
    aMap.put ("DG", "Diego Garc\u00eda");
    aMap.put ("DJ", "Djibuti");
    aMap.put ("DK", "Dinamarca");
    aMap.put ("DM", "Dominica");
    aMap.put ("DO", "Rep\u00fablica Dominicana");
    aMap.put ("DZ", "Arxelia");
    aMap.put ("EA", "Ceuta e Melilla");
    aMap.put ("EC", "Ecuador");
    aMap.put ("EE", "Estonia");
    aMap.put ("EG", "Exipto");
    aMap.put ("EH", "S\u00e1hara Occidental");
    aMap.put ("ER", "Eritrea");
    aMap.put ("ES", "Espa\u00f1a");
    aMap.put ("ET", "Etiop\u00eda");
    aMap.put ("EU", "Uni\u00f3n Europea");
    aMap.put ("FI", "Finlandia");
    aMap.put ("FJ", "Fidxi");
    aMap.put ("FK", "Illas Malvinas");
    aMap.put ("FM", "Micronesia");
    aMap.put ("FO", "Illas Feroe");
    aMap.put ("FR", "Francia");
    aMap.put ("GA", "Gab\u00f3n");
    aMap.put ("GB", "Reino Unido");
    aMap.put ("GD", "Granada");
    aMap.put ("GE", "Xeorxia");
    aMap.put ("GF", "G\u00fciana Francesa");
    aMap.put ("GG", "Guernsey");
    aMap.put ("GH", "Gana");
    aMap.put ("GI", "Xibraltar");
    aMap.put ("GL", "Grenlandia");
    aMap.put ("GM", "Gambia");
    aMap.put ("GN", "Guinea");
    aMap.put ("GP", "Guadalupe");
    aMap.put ("GQ", "Guinea Ecuatorial");
    aMap.put ("GR", "Grecia");
    aMap.put ("GS", "Xeorxia do Sur e Illas Sandwich");
    aMap.put ("GT", "Guatemala");
    aMap.put ("GU", "Guam");
    aMap.put ("GW", "Guinea-Bisau");
    aMap.put ("GY", "G\u00fciana");
    aMap.put ("HK", "Hong Kong RAE de China");
    aMap.put ("HM", "Illa Heard e Illas McDonald");
    aMap.put ("HN", "Honduras");
    aMap.put ("HR", "Croacia");
    aMap.put ("HT", "Hait\u00ed");
    aMap.put ("HU", "Hungr\u00eda");
    aMap.put ("IC", "Illas Canarias");
    aMap.put ("ID", "Indonesia");
    aMap.put ("IE", "Irlanda");
    aMap.put ("IL", "Israel");
    aMap.put ("IM", "Illa de Man");
    aMap.put ("IN", "India");
    aMap.put ("IO", "Territorio Brit\u00e1nico do Oc\u00e9ano \u00cdndico");
    aMap.put ("IQ", "Iraq");
    aMap.put ("IR", "Ir\u00e1n");
    aMap.put ("IS", "Islandia");
    aMap.put ("IT", "Italia");
    aMap.put ("JE", "Illa de Jersey");
    aMap.put ("JM", "Xamaica");
    aMap.put ("JO", "Xordania");
    aMap.put ("JP", "Xap\u00f3n");
    aMap.put ("KE", "Kenya");
    aMap.put ("KG", "Quirguicist\u00e1n");
    aMap.put ("KH", "Cambodia");
    aMap.put ("KI", "Kiribati");
    aMap.put ("KM", "Comores");
    aMap.put ("KN", "Saint Kitts e Nevis");
    aMap.put ("KP", "Corea do Norte");
    aMap.put ("KR", "Corea do Sur");
    aMap.put ("KW", "Kuwait");
    aMap.put ("KY", "Illas Caim\u00e1n");
    aMap.put ("KZ", "Casaquist\u00e1n");
    aMap.put ("LA", "Laos");
    aMap.put ("LB", "L\u00edbano");
    aMap.put ("LC", "Santa Luc\u00eda");
    aMap.put ("LI", "Liechtenstein");
    aMap.put ("LK", "Sri Lanka");
    aMap.put ("LR", "Liberia");
    aMap.put ("LS", "Lesoto");
    aMap.put ("LT", "Lituania");
    aMap.put ("LU", "Luxemburgo");
    aMap.put ("LV", "Letonia");
    aMap.put ("LY", "Libia");
    aMap.put ("MA", "Marrocos");
    aMap.put ("MC", "M\u00f3naco");
    aMap.put ("MD", "Moldavia");
    aMap.put ("ME", "Montenegro");
    aMap.put ("MF", "San Marti\u00f1o");
    aMap.put ("MG", "Madagascar");
    aMap.put ("MH", "Illas Marshall");
    aMap.put ("MK", "Rep\u00fablica de Macedonia");
    aMap.put ("ML", "Mal\u00ed");
    aMap.put ("MM", "Myanmar (Birmania)");
    aMap.put ("MN", "Mongolia");
    aMap.put ("MO", "Macau RAE de China");
    aMap.put ("MP", "Illas Marianas do norte");
    aMap.put ("MQ", "Martinica");
    aMap.put ("MR", "Mauritania");
    aMap.put ("MS", "Illa Montserrat");
    aMap.put ("MT", "Malta");
    aMap.put ("MU", "Mauricio");
    aMap.put ("MV", "Maldivas");
    aMap.put ("MW", "Malaui");
    aMap.put ("MX", "M\u00e9xico");
    aMap.put ("MY", "Malaisia");
    aMap.put ("MZ", "Mozambique");
    aMap.put ("NA", "Namibia");
    aMap.put ("NC", "Nova Caledonia");
    aMap.put ("NE", "N\u00edxer");
    aMap.put ("NF", "Illa Norfolk");
    aMap.put ("NG", "Nixeria");
    aMap.put ("NI", "Nicaragua");
    aMap.put ("NL", "Pa\u00edses Baixos");
    aMap.put ("NO", "Noruega");
    aMap.put ("NP", "Nepal");
    aMap.put ("NR", "Nauru");
    aMap.put ("NU", "Niue");
    aMap.put ("NZ", "Nova Zelandia");
    aMap.put ("OM", "Om\u00e1n");
    aMap.put ("PA", "Panam\u00e1");
    aMap.put ("PE", "Per\u00fa");
    aMap.put ("PF", "Polinesia Francesa");
    aMap.put ("PG", "Pap\u00faa Nova Guinea");
    aMap.put ("PH", "Filipinas");
    aMap.put ("PK", "Paquist\u00e1n");
    aMap.put ("PL", "Polonia");
    aMap.put ("PM", "San Pedro e Miguel\u00f3n");
    aMap.put ("PN", "Illas Pitcairn");
    aMap.put ("PR", "Porto Rico");
    aMap.put ("PS", "Territorios palestinos");
    aMap.put ("PT", "Portugal");
    aMap.put ("PW", "Rep. das Palaus");
    aMap.put ("PY", "Paraguai");
    aMap.put ("QA", "Qatar");
    aMap.put ("QO", "Ocean\u00eda Distante");
    aMap.put ("RE", "Reuni\u00f3n");
    aMap.put ("RO", "Roman\u00eda");
    aMap.put ("RS", "Serbia");
    aMap.put ("RU", "Rusia");
    aMap.put ("RW", "Ruanda");
    aMap.put ("SA", "Arabia Saudita");
    aMap.put ("SB", "Illas Salom\u00f3n");
    aMap.put ("SC", "Seixeles");
    aMap.put ("SD", "Sud\u00e1n");
    aMap.put ("SE", "Suecia");
    aMap.put ("SG", "Singapur");
    aMap.put ("SH", "Santa Helena");
    aMap.put ("SI", "Eslovenia");
    aMap.put ("SJ", "Svalbard e Jan Mayen");
    aMap.put ("SK", "Eslovaquia");
    aMap.put ("SL", "Serra Leoa");
    aMap.put ("SM", "San Marino");
    aMap.put ("SN", "Senegal");
    aMap.put ("SO", "Somalia");
    aMap.put ("SR", "Surinam");
    aMap.put ("SS", "Sud\u00e1n do sur");
    aMap.put ("ST", "San Tom\u00e9 e Pr\u00edncipe");
    aMap.put ("SV", "O Salvador");
    aMap.put ("SX", "Sint Maarten");
    aMap.put ("SY", "Siria");
    aMap.put ("SZ", "Suacilandia");
    aMap.put ("TA", "Trist\u00e1n da Cunha");
    aMap.put ("TC", "Illas Turks e Caicos");
    aMap.put ("TD", "Chad");
    aMap.put ("TF", "Territorios Franceses do Sul");
    aMap.put ("TG", "Togo");
    aMap.put ("TH", "Tailandia");
    aMap.put ("TJ", "Taxiquist\u00e1n");
    aMap.put ("TK", "Toquelau");
    aMap.put ("TL", "Timor Leste");
    aMap.put ("TM", "Turcomenist\u00e1n");
    aMap.put ("TN", "Tunisia");
    aMap.put ("TO", "Tonga");
    aMap.put ("TR", "Turqu\u00eda");
    aMap.put ("TT", "Trindade e Tobago");
    aMap.put ("TV", "Tuvalu");
    aMap.put ("TW", "Taiw\u00e1n");
    aMap.put ("TZ", "Tanzania");
    aMap.put ("UA", "Ucra\u00edna");
    aMap.put ("UG", "Uganda");
    aMap.put ("UM", "Illas Menores Distantes dos EUA.");
    aMap.put ("US", "Estados Unidos de Am\u00e9rica");
    aMap.put ("UY", "Uruguai");
    aMap.put ("UZ", "Uzbekist\u00e1n");
    aMap.put ("VA", "Cidade do Vaticano");
    aMap.put ("VC", "San Vicente e Granadinas");
    aMap.put ("VE", "Venezuela");
    aMap.put ("VG", "Illas Virxes Brit\u00e1nicas");
    aMap.put ("VI", "Illas Virxes Estadounidenses");
    aMap.put ("VN", "Vietnam");
    aMap.put ("VU", "Vanuatu");
    aMap.put ("WF", "Wallis e Futuna");
    aMap.put ("WS", "Samoa");
    aMap.put ("XK", "Kosovo");
    aMap.put ("YE", "Iemen");
    aMap.put ("YT", "Mayotte");
    aMap.put ("ZA", "Sud\u00e1frica");
    aMap.put ("ZM", "Zambia");
    aMap.put ("ZW", "Cimbabue");
    aMap.put ("ZZ", "Rexi\u00f3n desco\u00f1ecida");
    return aMap;
  }

  private static Map <String, String> _createDisplayLanguages ()
  {
    final Map <String, String> aMap = new HashMap <String, String> (241);
    aMap.put ("ab", "abkhazo");
    aMap.put ("ach", "acoli");
    aMap.put ("af", "afrikaans");
    aMap.put ("agq", "aghem");
    aMap.put ("ak", "ak\u00e1n");
    aMap.put ("am", "am\u00e1rico");
    aMap.put ("an", "aragon\u00e9s");
    aMap.put ("ar", "\u00e1rabe");
    aMap.put ("arc", "arameo");
    aMap.put ("arn", "mapuche");
    aMap.put ("as", "assam\u00e9s");
    aMap.put ("asa", "asu");
    aMap.put ("ast", "asturiano");
    aMap.put ("ay", "aimar\u00e1");
    aMap.put ("az", "acerbaixano");
    aMap.put ("ba", "baskir");
    aMap.put ("be", "bielorruso");
    aMap.put ("bem", "bemba");
    aMap.put ("bez", "bena");
    aMap.put ("bg", "b\u00falgaro");
    aMap.put ("bm", "bambara");
    aMap.put ("bn", "bengal\u00ed");
    aMap.put ("bo", "tibetano");
    aMap.put ("br", "bret\u00f3n");
    aMap.put ("brx", "bodo");
    aMap.put ("bs", "bosnio");
    aMap.put ("ca", "catal\u00e1n");
    aMap.put ("cgg", "kiga");
    aMap.put ("chr", "cheroqui");
    aMap.put ("ckb", "curdo soran\u00ed");
    aMap.put ("co", "corso");
    aMap.put ("cs", "checo");
    aMap.put ("cu", "eslavo eclesi\u00e1stico");
    aMap.put ("cy", "gal\u00e9s");
    aMap.put ("da", "dinamarqu\u00e9s");
    aMap.put ("dav", "taita");
    aMap.put ("de", "alem\u00e1n");
    aMap.put ("dje", "zarma");
    aMap.put ("dsb", "baixo sorabio");
    aMap.put ("dua", "duala");
    aMap.put ("dv", "divehi");
    aMap.put ("dyo", "jola-fonyi");
    aMap.put ("dz", "dzongkha");
    aMap.put ("ebu", "embu");
    aMap.put ("ee", "ew\u00e9");
    aMap.put ("efi", "ibibio");
    aMap.put ("egy", "exipcio antigo");
    aMap.put ("el", "grego");
    aMap.put ("en", "ingl\u00e9s");
    aMap.put ("eo", "esperanto");
    aMap.put ("es", "espa\u00f1ol");
    aMap.put ("et", "estoniano");
    aMap.put ("eu", "\u00e9uscaro");
    aMap.put ("fa", "persa");
    aMap.put ("fi", "fin\u00e9s");
    aMap.put ("fil", "filipino");
    aMap.put ("fj", "fixiano");
    aMap.put ("fo", "faro\u00e9s");
    aMap.put ("fr", "franc\u00e9s");
    aMap.put ("fy", "fris\u00f3n");
    aMap.put ("ga", "irland\u00e9s");
    aMap.put ("gaa", "ga");
    aMap.put ("gag", "gagauz");
    aMap.put ("gd", "ga\u00e9lico escoc\u00e9s");
    aMap.put ("gl", "galego");
    aMap.put ("gn", "guaran\u00ed");
    aMap.put ("grc", "grego antigo");
    aMap.put ("gsw", "alem\u00e1n su\u00edzo");
    aMap.put ("gu", "guxaratiano");
    aMap.put ("guz", "gusii");
    aMap.put ("gv", "manx");
    aMap.put ("ha", "hausa");
    aMap.put ("haw", "hawaiano");
    aMap.put ("he", "hebreo");
    aMap.put ("hi", "hindi");
    aMap.put ("hr", "croata");
    aMap.put ("hsb", "alto sorabio");
    aMap.put ("ht", "haitiano");
    aMap.put ("hu", "h\u00fangaro");
    aMap.put ("hy", "armenio");
    aMap.put ("ia", "interlingua");
    aMap.put ("id", "indonesio");
    aMap.put ("ig", "ibo");
    aMap.put ("ii", "yi sichuan\u00e9s");
    aMap.put ("is", "island\u00e9s");
    aMap.put ("it", "italiano");
    aMap.put ("iu", "inuit");
    aMap.put ("ja", "xapon\u00e9s");
    aMap.put ("jgo", "ngomba");
    aMap.put ("jmc", "machame");
    aMap.put ("jv", "xavan\u00e9s");
    aMap.put ("ka", "xeorxiano");
    aMap.put ("kab", "kabile");
    aMap.put ("kam", "kamba");
    aMap.put ("kde", "makonde");
    aMap.put ("kea", "caboverdiano");
    aMap.put ("kg", "kongo");
    aMap.put ("khq", "koyra chiini");
    aMap.put ("ki", "kikuyu");
    aMap.put ("kk", "casaco");
    aMap.put ("kl", "groenland\u00e9s");
    aMap.put ("kln", "kalenjin");
    aMap.put ("km", "cambodiano");
    aMap.put ("kn", "kannada");
    aMap.put ("ko", "coreano");
    aMap.put ("koi", "komi permio");
    aMap.put ("kok", "konkani");
    aMap.put ("ks", "cachemir");
    aMap.put ("ksb", "shambala");
    aMap.put ("ksf", "bafia");
    aMap.put ("ku", "kurdo");
    aMap.put ("kw", "c\u00f3rnico");
    aMap.put ("ky", "quirguiz");
    aMap.put ("la", "lat\u00edn");
    aMap.put ("lag", "langi");
    aMap.put ("lb", "luxemburgu\u00e9s");
    aMap.put ("lg", "ganda");
    aMap.put ("lkt", "lakota");
    aMap.put ("ln", "lingala");
    aMap.put ("lo", "laotiano");
    aMap.put ("loz", "lozi");
    aMap.put ("lt", "lituano");
    aMap.put ("lu", "luba-katanga");
    aMap.put ("lua", "luba-lulua");
    aMap.put ("luo", "luo");
    aMap.put ("luy", "luyia");
    aMap.put ("lv", "let\u00f3n");
    aMap.put ("mas", "masai");
    aMap.put ("mer", "meru");
    aMap.put ("mfe", "crioulo mauritano");
    aMap.put ("mg", "malgaxe");
    aMap.put ("mgh", "makhuwa-meetto");
    aMap.put ("mgo", "meta\u2019");
    aMap.put ("mi", "maor\u00ed");
    aMap.put ("mk", "macedonio");
    aMap.put ("ml", "malabar");
    aMap.put ("mn", "mongol");
    aMap.put ("moh", "mohawk");
    aMap.put ("mr", "marathi");
    aMap.put ("ms", "malaio");
    aMap.put ("mt", "malt\u00e9s");
    aMap.put ("mua", "mundang");
    aMap.put ("mul", "varias linguas");
    aMap.put ("my", "birmano");
    aMap.put ("naq", "nama");
    aMap.put ("nb", "noruegu\u00e9s bokmal");
    aMap.put ("nd", "ndebele do norte");
    aMap.put ("ne", "nepal\u00ed");
    aMap.put ("nl", "holand\u00e9s");
    aMap.put ("nmg", "kwasio");
    aMap.put ("nn", "noruegu\u00e9s nynorsk");
    aMap.put ("no", "noruegu\u00e9s");
    aMap.put ("nqo", "n\u2019ko");
    aMap.put ("nso", "sesotho sa leboa");
    aMap.put ("nus", "nuer");
    aMap.put ("ny", "chewa");
    aMap.put ("nyn", "nyankole");
    aMap.put ("oc", "occitano");
    aMap.put ("om", "oromo");
    aMap.put ("or", "oriya");
    aMap.put ("os", "osetio");
    aMap.put ("pa", "punjabi");
    aMap.put ("pl", "polaco");
    aMap.put ("ps", "paxt\u00fan");
    aMap.put ("pt", "portugu\u00e9s");
    aMap.put ("qu", "quechua");
    aMap.put ("quc", "quich\u00e9");
    aMap.put ("rm", "romanche");
    aMap.put ("rn", "rundi");
    aMap.put ("ro", "roman\u00e9s");
    aMap.put ("rof", "rombo");
    aMap.put ("ru", "ruso");
    aMap.put ("rw", "ruand\u00e9s");
    aMap.put ("rwk", "rwa");
    aMap.put ("sa", "s\u00e1nscrito");
    aMap.put ("saq", "samburu");
    aMap.put ("sbp", "sangu");
    aMap.put ("sd", "sindhi");
    aMap.put ("se", "sami do norte");
    aMap.put ("seh", "sena");
    aMap.put ("ses", "koyraboro senni");
    aMap.put ("sg", "sango");
    aMap.put ("sh", "serbocroata");
    aMap.put ("shi", "tachelhit");
    aMap.put ("si", "cingal\u00e9s");
    aMap.put ("sk", "eslovaco");
    aMap.put ("sl", "esloveno");
    aMap.put ("sm", "samoano");
    aMap.put ("sma", "saami do sur");
    aMap.put ("smj", "saami lule");
    aMap.put ("smn", "saami inari");
    aMap.put ("sms", "saami skolt");
    aMap.put ("sn", "shona");
    aMap.put ("so", "somal\u00ed");
    aMap.put ("sq", "alban\u00e9s");
    aMap.put ("sr", "serbio");
    aMap.put ("ss", "swati");
    aMap.put ("st", "sesoto");
    aMap.put ("su", "sondan\u00e9s");
    aMap.put ("sv", "sueco");
    aMap.put ("sw", "swahili");
    aMap.put ("swc", "suahili congol\u00e9s");
    aMap.put ("ta", "tamil");
    aMap.put ("te", "telugu");
    aMap.put ("teo", "teso");
    aMap.put ("tet", "tet\u00fan");
    aMap.put ("tg", "taxico");
    aMap.put ("th", "tailand\u00e9s");
    aMap.put ("ti", "tigri\u00f1a");
    aMap.put ("tk", "turcomano");
    aMap.put ("tl", "tagalo");
    aMap.put ("tlh", "klingon");
    aMap.put ("tn", "tswana");
    aMap.put ("to", "tongan\u00e9s");
    aMap.put ("tpi", "tok pisin");
    aMap.put ("tr", "turco");
    aMap.put ("ts", "xitsonga");
    aMap.put ("tt", "t\u00e1rtaro");
    aMap.put ("tum", "tumbuka");
    aMap.put ("tw", "twi");
    aMap.put ("twq", "tasawaq");
    aMap.put ("ty", "tahitiano");
    aMap.put ("tzm", "tamazight marroqu\u00ed central");
    aMap.put ("ug", "uigur");
    aMap.put ("uk", "ucra\u00edno");
    aMap.put ("und", "lingua desco\u00f1ecida ou non v\u00e1lida");
    aMap.put ("ur", "urd\u00fa");
    aMap.put ("uz", "uzbeco");
    aMap.put ("vai", "vai");
    aMap.put ("ve", "venda");
    aMap.put ("vi", "vietnamita");
    aMap.put ("vun", "vunjo");
    aMap.put ("wo", "w\u00f3lof");
    aMap.put ("xh", "xhosa");
    aMap.put ("xog", "soga");
    aMap.put ("yi", "yiddish");
    aMap.put ("yo", "ioruba");
    aMap.put ("zgh", "tamazight de Marrocos est\u00e1ndar");
    aMap.put ("zh", "chin\u00e9s");
    aMap.put ("zu", "zul\u00fa");
    aMap.put ("zxx", "sen contido ling\u00fc\u00edstico");
    return aMap;
  }

  private static final Map <String, String> DISPLAY_COUNTRIES = _createDisplayCountries ();
  private static final Map <String, String> DISPLAY_LANGUAGES = _createDisplayLanguages ();

  public GalicianLocaleNameProvider ()
  {}

  @Override
  public String getDisplayCountry (final String sCountryCode, final Locale aLocale)
  {
    if (aLocale == null)
      throw new NullPointerException ();

    if (GalicianLocales.GALICIAN_LIST.contains (aLocale))
      return DISPLAY_COUNTRIES.get (sCountryCode);

    throw new IllegalArgumentException ("Locale \"" +
                                        aLocale +
                                        "\" " +
                                        "is not one of the supported locales (" +
                                        GalicianLocales.GALICIAN_LIST +
                                        ")");
  }

  @Override
  public String getDisplayLanguage (final String sLanguageCode, final Locale aLocale)
  {
    if (aLocale == null)
      throw new NullPointerException ();

    if (GalicianLocales.GALICIAN_LIST.contains (aLocale))
      return DISPLAY_LANGUAGES.get (sLanguageCode);

    throw new IllegalArgumentException ("Locale \"" +
                                        aLocale +
                                        "\" " +
                                        "is not one of the supported locales (" +
                                        GalicianLocales.GALICIAN_LIST +
                                        ")");
  }

  @Override
  public String getDisplayVariant (final String sVariant, final Locale aLocale)
  {
    if (aLocale == null)
      throw new NullPointerException ();

    if (GalicianLocales.GALICIAN_LIST.contains (aLocale))
      return sVariant;

    throw new IllegalArgumentException ("Locale \"" +
                                        aLocale +
                                        "\" " +
                                        "is not one of the supported locales (" +
                                        GalicianLocales.GALICIAN_LIST +
                                        ")");
  }

  @Override
  public Locale [] getAvailableLocales ()
  {
    return GalicianLocales.getLocaleArray ();
  }
}
