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
package com.helger.locales.sh;

import java.text.Collator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.helger.locales.localedata.ILocaleData;

/**
 * Various locale settings for the Serbocroatian languages (mostly Bosnian
 * usage).
 *
 * @author Klaus Brunner
 */
public class LocalDataSerbocroatian implements ILocaleData
{
  static
  {
    // Emit an error message if this is used with a Java version &gt; 1.7
    if (Double.parseDouble (System.getProperty ("java.specification.version")) > 1.6)
      System.err.println ("The Serbocroatian Locale extension can only be used with Java up to version 1.6. Starting with Java 1.7 the support for this locale is present using the Locale sr_Latn etc.");
  }

  protected static final String SERBOCROATIAN_LANGUAGE = "sh";
  protected static final String BOSNIAN_LANGUAGE = "bs";
  protected static final String CROATIA_COUNTRY = "HR";
  private static final Locale HR_LOCALE = new Locale (CROATIA_COUNTRY);

  private static final Locale [] SUPPORTED_LOCALES = { new Locale (BOSNIAN_LANGUAGE),
                                                       new Locale (SERBOCROATIAN_LANGUAGE) };

  private static final String [] ERAS = { "p.n.e.", "n.e." };

  private static final String PATTERN_CHARS = "GanjkHmsSEDFwWxhKzZ";

  private static final String [] MONTHS = { "januar",
                                            "februar",
                                            "mart",
                                            "april",
                                            "maj",
                                            "juni",
                                            "juli",
                                            "avgust",
                                            "septembar",
                                            "oktobar",
                                            "novembar",
                                            "decembar" };

  private static final String [] SHORT_MONTHS = { "jan",
                                                  "feb",
                                                  "mar",
                                                  "apr",
                                                  "maj",
                                                  "jun",
                                                  "jul",
                                                  "avg",
                                                  "sep",
                                                  "okt",
                                                  "nov",
                                                  "dec" };

  private static final String [] WEEKDAYS = { "",
                                              "nedjelja",
                                              "ponedjeljak",
                                              "utorak",
                                              "srijeda",
                                              "četvrtak",
                                              "petak",
                                              "subota" };

  private static final String [] SHORT_WEEKDAYS = { "", "ned", "pon", "uto", "sri", "čet", "pet", "sub" };

  private static final String DATE_SHORT = "dd.MM.yy.";
  private static final String DATE_MEDIUM = "dd.MM.yyyy.";
  private static final String DATE_LONG = "d. MMMM yyyy.";
  private static final String DATE_FULL = "EEEE, d. MMMM yyyy.";

  private static final String TIME_SHORT = "HH:mm";
  private static final String TIME_MEDIUM = "HH:mm:ss";
  private static final String TIME_LONG = "HH:mm:ss z";
  private static final String TIME_FULL = "HH:mm:ss z";

  private static final char DECIMAL_SEPARATOR = ',';
  private static final char GROUPING_SEPARATOR = '.';
  private static final char PATTERN_SEPARATOR = ';';
  private static final char PERCENT = '%';
  private static final char ZERO_DIGIT = '0';
  private static final char DIGIT = '#';
  private static final char MINUS_SIGN = '-';
  private static final String EXPONENT_SEPARATOR = "E";
  private static final char PER_MILLE = '‰';
  private static final String INFINITY = "∞";
  // TODO: is there anything better?
  private static final String NAN = "NaN";
  // generic currency symbol
  private static final String CURRENCY_SYMBOL = "¤";
  private static final String PERCENT_FORMAT = "#,##0%";
  private static final String NUMBER_FORMAT = "#,##0.###";
  private static final String INTEGER_FORMAT = "#,##0";
  private static final String CURRENCY_FORMAT = "¤ #,##0.00";

  private static Map <String, String> _createDisplayCountries ()
  {
    final Map <String, String> aMap = new HashMap <String, String> (260);
    aMap.put ("AC", "Ostrvo Asension");
    aMap.put ("AD", "Andora");
    aMap.put ("AE", "Ujedinjeni Arapski Emirati");
    aMap.put ("AF", "Avganistan");
    aMap.put ("AG", "Antigva i Barbuda");
    aMap.put ("AI", "Angvila");
    aMap.put ("AL", "Albanija");
    aMap.put ("AM", "Jermenija");
    aMap.put ("AN", "Holandski Antili");
    aMap.put ("AO", "Angola");
    aMap.put ("AQ", "Antarktik");
    aMap.put ("AR", "Argentina");
    aMap.put ("AS", "Ameri\u010dka Samoa");
    aMap.put ("AT", "Austrija");
    aMap.put ("AU", "Australija");
    aMap.put ("AW", "Aruba");
    aMap.put ("AX", "Olandska ostrva");
    aMap.put ("AZ", "Azerbejd\u017ean");
    aMap.put ("BA", "Bosna i Hercegovina");
    aMap.put ("BB", "Barbados");
    aMap.put ("BD", "Banglade\u0161");
    aMap.put ("BE", "Belgija");
    aMap.put ("BF", "Burkina Faso");
    aMap.put ("BG", "Bugarska");
    aMap.put ("BH", "Bahrein");
    aMap.put ("BI", "Burundi");
    aMap.put ("BJ", "Benin");
    aMap.put ("BL", "Sveti Bartolomej");
    aMap.put ("BM", "Bermuda");
    aMap.put ("BN", "Brunej");
    aMap.put ("BO", "Bolivija");
    aMap.put ("BQ", "Karipska Holandija");
    aMap.put ("BR", "Brazil");
    aMap.put ("BS", "Bahami");
    aMap.put ("BT", "Butan");
    aMap.put ("BV", "Ostrvo Buve");
    aMap.put ("BW", "Bocvana");
    aMap.put ("BY", "Belorusija");
    aMap.put ("BZ", "Belize");
    aMap.put ("CA", "Kanada");
    aMap.put ("CC", "Kokos (Keling) Ostrva");
    aMap.put ("CD", "Kongo - Kin\u0161asa");
    aMap.put ("CF", "Centralnoafri\u010dka Republika");
    aMap.put ("CG", "Kongo - Brazavil");
    aMap.put ("CH", "\u0160vajcarska");
    aMap.put ("CI", "Obala Slonova\u010de");
    aMap.put ("CK", "Kukova Ostrva");
    aMap.put ("CL", "\u010cile");
    aMap.put ("CM", "Kamerun");
    aMap.put ("CN", "Kina");
    aMap.put ("CO", "Kolumbija");
    aMap.put ("CP", "Ostrvo Kliperton");
    aMap.put ("CR", "Kostarika");
    aMap.put ("CU", "Kuba");
    aMap.put ("CV", "Zelenortska Ostrva");
    aMap.put ("CW", "Kurasao");
    aMap.put ("CX", "Bo\u017ei\u0107no ostrvo");
    aMap.put ("CY", "Kipar");
    aMap.put ("CZ", "\u010ce\u0161ka");
    aMap.put ("DE", "Nema\u010dka");
    aMap.put ("DG", "Dijego Garsija");
    aMap.put ("DJ", "D\u017eibuti");
    aMap.put ("DK", "Danska");
    aMap.put ("DM", "Dominika");
    aMap.put ("DO", "Dominikanska Republika");
    aMap.put ("DZ", "Al\u017eir");
    aMap.put ("EA", "Seuta i Melilja");
    aMap.put ("EC", "Ekvador");
    aMap.put ("EE", "Estonija");
    aMap.put ("EG", "Egipat");
    aMap.put ("EH", "Zapadna Sahara");
    aMap.put ("ER", "Eritreja");
    aMap.put ("ES", "\u0160panija");
    aMap.put ("ET", "Etiopija");
    aMap.put ("EU", "Evropska Unija");
    aMap.put ("FI", "Finska");
    aMap.put ("FJ", "Fid\u017ei");
    aMap.put ("FK", "Foklandska ostrva");
    aMap.put ("FM", "Mikronezija");
    aMap.put ("FO", "Farska Ostrva");
    aMap.put ("FR", "Francuska");
    aMap.put ("GA", "Gabon");
    aMap.put ("GB", "Velika Britanija");
    aMap.put ("GD", "Grenada");
    aMap.put ("GE", "Gruzija");
    aMap.put ("GF", "Francuska Gvajana");
    aMap.put ("GG", "Gurnsi");
    aMap.put ("GH", "Gana");
    aMap.put ("GI", "Gibraltar");
    aMap.put ("GL", "Grenland");
    aMap.put ("GM", "Gambija");
    aMap.put ("GN", "Gvineja");
    aMap.put ("GP", "Gvadelupe");
    aMap.put ("GQ", "Ekvatorijalna Gvineja");
    aMap.put ("GR", "Gr\u010dka");
    aMap.put ("GS", "Ju\u017ena D\u017eord\u017eija i Ju\u017ena Sendvi\u010d Ostrva");
    aMap.put ("GT", "Gvatemala");
    aMap.put ("GU", "Guam");
    aMap.put ("GW", "Gvineja-Bisao");
    aMap.put ("GY", "Gvajana");
    aMap.put ("HK", "Hong Kong S. A. R. Kina");
    aMap.put ("HM", "Ostrva Herd i Mekdonald");
    aMap.put ("HN", "Honduras");
    aMap.put ("HR", "Hrvatska");
    aMap.put ("HT", "Haiti");
    aMap.put ("HU", "Ma\u0111arska");
    aMap.put ("IC", "Kanarska ostrva");
    aMap.put ("ID", "Indonezija");
    aMap.put ("IE", "Irska");
    aMap.put ("IL", "Izrael");
    aMap.put ("IM", "Ostrvo Man");
    aMap.put ("IN", "Indija");
    aMap.put ("IO", "Britanska teritorija u Indijskom okeanu");
    aMap.put ("IQ", "Irak");
    aMap.put ("IR", "Iran");
    aMap.put ("IS", "Island");
    aMap.put ("IT", "Italija");
    aMap.put ("JE", "D\u017eersi");
    aMap.put ("JM", "Jamajka");
    aMap.put ("JO", "Jordan");
    aMap.put ("JP", "Japan");
    aMap.put ("KE", "Kenija");
    aMap.put ("KG", "Kirgistan");
    aMap.put ("KH", "Kambod\u017ea");
    aMap.put ("KI", "Kiribati");
    aMap.put ("KM", "Komorska Ostrva");
    aMap.put ("KN", "Sent Kits i Nevis");
    aMap.put ("KP", "Severna Koreja");
    aMap.put ("KR", "Ju\u017ena Koreja");
    aMap.put ("KW", "Kuvajt");
    aMap.put ("KY", "Kajmanska Ostrva");
    aMap.put ("KZ", "Kazahstan");
    aMap.put ("LA", "Laos");
    aMap.put ("LB", "Liban");
    aMap.put ("LC", "Sveta Lucija");
    aMap.put ("LI", "Lihten\u0161tajn");
    aMap.put ("LK", "\u0160ri Lanka");
    aMap.put ("LR", "Liberija");
    aMap.put ("LS", "Lesoto");
    aMap.put ("LT", "Litvanija");
    aMap.put ("LU", "Luksemburg");
    aMap.put ("LV", "Letonija");
    aMap.put ("LY", "Libija");
    aMap.put ("MA", "Maroko");
    aMap.put ("MC", "Monako");
    aMap.put ("MD", "Moldavija");
    aMap.put ("ME", "Crna Gora");
    aMap.put ("MF", "Sent Martin");
    aMap.put ("MG", "Madagaskar");
    aMap.put ("MH", "Mar\u0161alska Ostrva");
    aMap.put ("MK", "Makedonija");
    aMap.put ("ML", "Mali");
    aMap.put ("MM", "Mijanmar (Burma)");
    aMap.put ("MN", "Mongolija");
    aMap.put ("MO", "SAR Makao (Kina)");
    aMap.put ("MP", "Severna Marijanska Ostrva");
    aMap.put ("MQ", "Martinik");
    aMap.put ("MR", "Mauritanija");
    aMap.put ("MS", "Monserat");
    aMap.put ("MT", "Malta");
    aMap.put ("MU", "Mauricijus");
    aMap.put ("MV", "Maldivi");
    aMap.put ("MW", "Malavi");
    aMap.put ("MX", "Meksiko");
    aMap.put ("MY", "Malezija");
    aMap.put ("MZ", "Mozambik");
    aMap.put ("NA", "Namibija");
    aMap.put ("NC", "Nova Kaledonija");
    aMap.put ("NE", "Niger");
    aMap.put ("NF", "Norfolk Ostrvo");
    aMap.put ("NG", "Nigerija");
    aMap.put ("NI", "Nikaragva");
    aMap.put ("NL", "Holandija");
    aMap.put ("NO", "Norve\u0161ka");
    aMap.put ("NP", "Nepal");
    aMap.put ("NR", "Nauru");
    aMap.put ("NU", "Niue");
    aMap.put ("NZ", "Novi Zeland");
    aMap.put ("OM", "Oman");
    aMap.put ("PA", "Panama");
    aMap.put ("PE", "Peru");
    aMap.put ("PF", "Francuska Polinezija");
    aMap.put ("PG", "Papua Nova Gvineja");
    aMap.put ("PH", "Filipini");
    aMap.put ("PK", "Pakistan");
    aMap.put ("PL", "Poljska");
    aMap.put ("PM", "Sen Pjer i Mikelon");
    aMap.put ("PN", "Pitkern");
    aMap.put ("PR", "Portoriko");
    aMap.put ("PS", "Palestinske teritorije");
    aMap.put ("PT", "Portugal");
    aMap.put ("PW", "Palau");
    aMap.put ("PY", "Paragvaj");
    aMap.put ("QA", "Katar");
    aMap.put ("QO", "Okeanija (udaljena ostrva)");
    aMap.put ("RE", "Reinion");
    aMap.put ("RO", "Rumunija");
    aMap.put ("RS", "Srbija");
    aMap.put ("RU", "Rusija");
    aMap.put ("RW", "Ruanda");
    aMap.put ("SA", "Saudijska Arabija");
    aMap.put ("SB", "Solomonska Ostrva");
    aMap.put ("SC", "Sej\u0161eli");
    aMap.put ("SD", "Sudan");
    aMap.put ("SE", "\u0160vedska");
    aMap.put ("SG", "Singapur");
    aMap.put ("SH", "Sveta Jelena");
    aMap.put ("SI", "Slovenija");
    aMap.put ("SJ", "Svalbard i Jan Majen");
    aMap.put ("SK", "Slova\u010dka");
    aMap.put ("SL", "Sijera Leone");
    aMap.put ("SM", "San Marino");
    aMap.put ("SN", "Senegal");
    aMap.put ("SO", "Somalija");
    aMap.put ("SR", "Surinam");
    aMap.put ("SS", "Ju\u017eni Sudan");
    aMap.put ("ST", "Sao Tome i Principe");
    aMap.put ("SV", "Salvador");
    aMap.put ("SX", "Sveti Martin");
    aMap.put ("SY", "Sirija");
    aMap.put ("SZ", "Svazilend");
    aMap.put ("TA", "Tristan da Kunja");
    aMap.put ("TC", "Ostrva Turks i Kaikos");
    aMap.put ("TD", "\u010cad");
    aMap.put ("TF", "Francuske Ju\u017ene Teritorije");
    aMap.put ("TG", "Togo");
    aMap.put ("TH", "Tajland");
    aMap.put ("TJ", "Tad\u017eikistan");
    aMap.put ("TK", "Tokelau");
    aMap.put ("TL", "Isto\u010dni Timor");
    aMap.put ("TM", "Turkmenistan");
    aMap.put ("TN", "Tunis");
    aMap.put ("TO", "Tonga");
    aMap.put ("TR", "Turska");
    aMap.put ("TT", "Trinidad i Tobago");
    aMap.put ("TV", "Tuvalu");
    aMap.put ("TW", "Tajvan");
    aMap.put ("TZ", "Tanzanija");
    aMap.put ("UA", "Ukrajina");
    aMap.put ("UG", "Uganda");
    aMap.put ("UM", "Udaljena ostrva SAD");
    aMap.put ("US", "Sjedinjene Ameri\u010dke Dr\u017eave");
    aMap.put ("UY", "Urugvaj");
    aMap.put ("UZ", "Uzbekistan");
    aMap.put ("VA", "Vatikan");
    aMap.put ("VC", "Sent Vinsent i Grenadini");
    aMap.put ("VE", "Venecuela");
    aMap.put ("VG", "Britanska Devi\u010danska Ostrva");
    aMap.put ("VI", "Ameri\u010dka Devi\u010danska Ostrva");
    aMap.put ("VN", "Vijetnam");
    aMap.put ("VU", "Vanuatu");
    aMap.put ("WF", "Valis i Futuna");
    aMap.put ("WS", "Samoa");
    aMap.put ("XK", "Kosovo");
    aMap.put ("YE", "Jemen");
    aMap.put ("YT", "Majot");
    aMap.put ("ZA", "Ju\u017enoafri\u010dka Republika");
    aMap.put ("ZM", "Zambija");
    aMap.put ("ZW", "Zimbabve");
    aMap.put ("ZZ", "Nepoznat region");
    return aMap;
  }

  private static Map <String, String> _createDisplayLanguages ()
  {
    final Map <String, String> aMap = new HashMap <String, String> (473);
    aMap.put ("aa", "Afarski");
    aMap.put ("ab", "abhaski");
    aMap.put ("ace", "A\u010dineski");
    aMap.put ("ach", "Akoli");
    aMap.put ("ada", "Adangmejski");
    aMap.put ("ady", "Adigejski");
    aMap.put ("ae", "Avestanski");
    aMap.put ("af", "afrikans");
    aMap.put ("afh", "Afrihili");
    aMap.put ("agq", "agem");
    aMap.put ("ain", "Ainu");
    aMap.put ("ak", "akan");
    aMap.put ("akk", "Akadijski");
    aMap.put ("ale", "Aljut");
    aMap.put ("alt", "Ju\u017eni altai");
    aMap.put ("am", "amharski");
    aMap.put ("an", "Aragone\u017eanski");
    aMap.put ("ang", "Staroengleski");
    aMap.put ("anp", "Angika");
    aMap.put ("ar", "arapski");
    aMap.put ("arc", "Armajski");
    aMap.put ("arn", "mapu\u010de");
    aMap.put ("arp", "Arapaho");
    aMap.put ("arw", "Aravak");
    aMap.put ("as", "asamski");
    aMap.put ("asa", "asu");
    aMap.put ("ast", "Asturijski");
    aMap.put ("av", "Avarski");
    aMap.put ("awa", "Avadhi");
    aMap.put ("ay", "Ajmara");
    aMap.put ("az", "azerbejd\u017eanski");
    aMap.put ("ba", "ba\u0161kirski");
    aMap.put ("bal", "Balu\u010di");
    aMap.put ("ban", "Balinezijski");
    aMap.put ("bas", "Basa");
    aMap.put ("be", "beloruski");
    aMap.put ("bej", "Beja");
    aMap.put ("bem", "bemba");
    aMap.put ("bez", "bena");
    aMap.put ("bg", "bugarski");
    aMap.put ("bho", "Bojpuri");
    aMap.put ("bi", "Bislama");
    aMap.put ("bik", "Bikol");
    aMap.put ("bin", "Bini");
    aMap.put ("bla", "Sisika");
    aMap.put ("bm", "bambara");
    aMap.put ("bn", "bengalski");
    aMap.put ("bo", "tibetanski");
    aMap.put ("br", "bretonski");
    aMap.put ("bra", "Braj");
    aMap.put ("brx", "bodo");
    aMap.put ("bs", "bosanski");
    aMap.put ("bua", "Buriat");
    aMap.put ("bug", "Bugine\u017eanski");
    aMap.put ("byn", "Blin");
    aMap.put ("ca", "katalonski");
    aMap.put ("cad", "Kado");
    aMap.put ("car", "Karipski");
    aMap.put ("cch", "Atsamski");
    aMap.put ("ce", "\u010ce\u010denski");
    aMap.put ("ceb", "Cebuano");
    aMap.put ("cgg", "\u010diga");
    aMap.put ("ch", "\u010camoro");
    aMap.put ("chb", "\u010cib\u010da");
    aMap.put ("chg", "\u010cagatai");
    aMap.put ("chk", "\u010cukeski");
    aMap.put ("chm", "Mari");
    aMap.put ("chn", "\u010cinukski");
    aMap.put ("cho", "\u010coktavski");
    aMap.put ("chp", "\u010cipvijanski");
    aMap.put ("chr", "\u010deroki");
    aMap.put ("chy", "\u010cejenski");
    aMap.put ("ckb", "sorani kurdski");
    aMap.put ("co", "korzikanski");
    aMap.put ("cop", "Koptski");
    aMap.put ("cr", "Kri");
    aMap.put ("crh", "Krimeanski turski");
    aMap.put ("cs", "\u010de\u0161ki");
    aMap.put ("csb", "Ka\u0161ubijanski");
    aMap.put ("cu", "Staroslovenski");
    aMap.put ("cv", "\u010cuva\u0161ki");
    aMap.put ("cy", "vel\u0161ki");
    aMap.put ("da", "danski");
    aMap.put ("dak", "Dakota");
    aMap.put ("dar", "Dargva");
    aMap.put ("dav", "taita");
    aMap.put ("de", "nema\u010dki");
    aMap.put ("del", "Delaver");
    aMap.put ("den", "Slavski");
    aMap.put ("dgr", "Dogrib");
    aMap.put ("din", "Dinka");
    aMap.put ("dje", "zarma");
    aMap.put ("doi", "Dogri");
    aMap.put ("dsb", "donji lu\u017ei\u010dkosrpski");
    aMap.put ("dua", "duala");
    aMap.put ("dum", "Srednji holandski");
    aMap.put ("dv", "Divehijski");
    aMap.put ("dyo", "d\u017eola fonji");
    aMap.put ("dyu", "\u0110ula");
    aMap.put ("dz", "d\u017eonga");
    aMap.put ("ebu", "embu");
    aMap.put ("ee", "eve");
    aMap.put ("efi", "Efikski");
    aMap.put ("egy", "Staroegipatski");
    aMap.put ("eka", "Ekajuk");
    aMap.put ("el", "gr\u010dki");
    aMap.put ("elx", "Elamitski");
    aMap.put ("en", "engleski");
    aMap.put ("enm", "Srednji engleski");
    aMap.put ("eo", "esperanto");
    aMap.put ("es", "\u0161panski");
    aMap.put ("et", "estonski");
    aMap.put ("eu", "baskijski");
    aMap.put ("ewo", "Evondo");
    aMap.put ("fa", "persijski");
    aMap.put ("fan", "Fang");
    aMap.put ("fat", "Fanti");
    aMap.put ("ff", "Fulah");
    aMap.put ("fi", "finski");
    aMap.put ("fil", "filipinski");
    aMap.put ("fj", "fid\u017eijski");
    aMap.put ("fo", "farski");
    aMap.put ("fon", "Fon");
    aMap.put ("fr", "francuski");
    aMap.put ("frm", "Srednji francuski");
    aMap.put ("fro", "Starofrancuski");
    aMap.put ("frr", "Severno-frizijski");
    aMap.put ("frs", "Isto\u010dni frizijski");
    aMap.put ("fur", "Friulijski");
    aMap.put ("fy", "zapadni frizijski");
    aMap.put ("ga", "irski");
    aMap.put ("gaa", "Ga");
    aMap.put ("gag", "gagauz");
    aMap.put ("gay", "Gajo");
    aMap.put ("gba", "Gbaja");
    aMap.put ("gd", "\u0160kotski Galski");
    aMap.put ("gez", "D\u017eiz");
    aMap.put ("gil", "Gilbert\u0161ki");
    aMap.put ("gl", "galicijski");
    aMap.put ("gmh", "Srednji visoki nema\u010dki");
    aMap.put ("gn", "gvarani");
    aMap.put ("goh", "Staronema\u010dki");
    aMap.put ("gon", "Gondi");
    aMap.put ("gor", "Gorontalo");
    aMap.put ("got", "Gotski");
    aMap.put ("grb", "Grebo");
    aMap.put ("grc", "Starogr\u010dki");
    aMap.put ("gsw", "\u0160vajcarski nema\u010dki");
    aMap.put ("gu", "gud\u017earati");
    aMap.put ("guz", "gusi");
    aMap.put ("gv", "Manks");
    aMap.put ("gwi", "Gvi\u010d\u2019in");
    aMap.put ("ha", "hausa");
    aMap.put ("hai", "Haida");
    aMap.put ("haw", "havajski");
    aMap.put ("he", "hebrejski");
    aMap.put ("hi", "hindi");
    aMap.put ("hil", "Hiligajnon");
    aMap.put ("hit", "Hitite");
    aMap.put ("hmn", "Hmong");
    aMap.put ("ho", "Hiri Motu");
    aMap.put ("hr", "hrvatski");
    aMap.put ("hsb", "gornji lu\u017ei\u010dkosrpski");
    aMap.put ("ht", "Haitski");
    aMap.put ("hu", "ma\u0111arski");
    aMap.put ("hup", "Hupa");
    aMap.put ("hy", "jermenski");
    aMap.put ("hz", "Herero");
    aMap.put ("ia", "Interlingva");
    aMap.put ("iba", "Iban");
    aMap.put ("id", "indone\u017eanski");
    aMap.put ("ie", "Me\u0111ujezi\u010dki");
    aMap.put ("ig", "igbo");
    aMap.put ("ii", "se\u010duan ji");
    aMap.put ("ik", "Unupiak");
    aMap.put ("ilo", "Iloko");
    aMap.put ("inh", "Ingvi\u0161ki");
    aMap.put ("io", "Ido");
    aMap.put ("is", "islandski");
    aMap.put ("it", "italijanski");
    aMap.put ("iu", "inuktitut");
    aMap.put ("ja", "japanski");
    aMap.put ("jbo", "Lojban");
    aMap.put ("jgo", "ngomba");
    aMap.put ("jmc", "ma\u010dame");
    aMap.put ("jpr", "Judeo-persijski");
    aMap.put ("jrb", "Judeo-arapski");
    aMap.put ("jv", "javanski");
    aMap.put ("ka", "gruzijski");
    aMap.put ("kaa", "Kara-kalpa\u0161ki");
    aMap.put ("kab", "kabile");
    aMap.put ("kac", "Ka\u010din");
    aMap.put ("kaj", "\u0110u");
    aMap.put ("kam", "kamba");
    aMap.put ("kaw", "Kavi");
    aMap.put ("kbd", "Kabardijski");
    aMap.put ("kcg", "Tjap");
    aMap.put ("kde", "makonde");
    aMap.put ("kea", "zelenortski kreolski");
    aMap.put ("kfo", "Koro");
    aMap.put ("kg", "Kongo");
    aMap.put ("kha", "Kasi");
    aMap.put ("kho", "Kotane\u0161ki");
    aMap.put ("khq", "kojra \u010diini");
    aMap.put ("ki", "kikuju");
    aMap.put ("kj", "Kuanjama");
    aMap.put ("kk", "kaza\u0161ki");
    aMap.put ("kl", "kalalisut");
    aMap.put ("kln", "kalend\u017ein");
    aMap.put ("km", "kmerski");
    aMap.put ("kmb", "Kimbundu");
    aMap.put ("kn", "kanada");
    aMap.put ("ko", "korejski");
    aMap.put ("koi", "komi-permski");
    aMap.put ("kok", "konkani");
    aMap.put ("kos", "Kosreanski");
    aMap.put ("kpe", "Kpele");
    aMap.put ("kr", "Kanuri");
    aMap.put ("krc", "Kara\u010daj-balkar");
    aMap.put ("kri", "Krio");
    aMap.put ("krl", "Karelijski");
    aMap.put ("kru", "Kurukh");
    aMap.put ("ks", "ka\u0161mirski");
    aMap.put ("ksb", "\u0161ambala");
    aMap.put ("ksf", "bafija");
    aMap.put ("ku", "kurdski");
    aMap.put ("kum", "Kumik");
    aMap.put ("kut", "Kutenai");
    aMap.put ("kv", "Komi");
    aMap.put ("kw", "kornvolski");
    aMap.put ("ky", "kirgiski");
    aMap.put ("la", "latinski");
    aMap.put ("lad", "Ladino");
    aMap.put ("lag", "langi");
    aMap.put ("lah", "Landa");
    aMap.put ("lam", "Lamba");
    aMap.put ("lb", "luksembur\u0161ki");
    aMap.put ("lez", "Lezgian");
    aMap.put ("lg", "ganda");
    aMap.put ("li", "Limburgi\u0161");
    aMap.put ("lkt", "lakota");
    aMap.put ("ln", "lingala");
    aMap.put ("lo", "lao\u0161ki");
    aMap.put ("lol", "Mongo");
    aMap.put ("loz", "Lozi");
    aMap.put ("lt", "litvanski");
    aMap.put ("lu", "luba-katanga");
    aMap.put ("lua", "Luba-lulua");
    aMap.put ("lui", "Luiseno");
    aMap.put ("lun", "Lunda");
    aMap.put ("luo", "luo");
    aMap.put ("lus", "Lu\u0161ai");
    aMap.put ("luy", "lujia");
    aMap.put ("lv", "letonski");
    aMap.put ("mad", "Madure\u0161ki");
    aMap.put ("mag", "Magahi");
    aMap.put ("mai", "Maitili");
    aMap.put ("mak", "Makasar");
    aMap.put ("man", "Mandingo");
    aMap.put ("mas", "masai");
    aMap.put ("mdf", "Mok\u0161a");
    aMap.put ("mdr", "Mandar");
    aMap.put ("men", "Mende");
    aMap.put ("mer", "meru");
    aMap.put ("mfe", "morisjen");
    aMap.put ("mg", "malga\u0161ki");
    aMap.put ("mga", "Srednji irski");
    aMap.put ("mgh", "makuva-meeto");
    aMap.put ("mgo", "meta");
    aMap.put ("mh", "Mar\u0161alski");
    aMap.put ("mi", "maorski");
    aMap.put ("mic", "Mikmak");
    aMap.put ("min", "Minangkabau");
    aMap.put ("mk", "makedonski");
    aMap.put ("ml", "malajalam");
    aMap.put ("mn", "mongolski");
    aMap.put ("mnc", "Man\u010du");
    aMap.put ("mni", "Manipuri");
    aMap.put ("moh", "mohok");
    aMap.put ("mos", "Mosi");
    aMap.put ("mr", "marati");
    aMap.put ("ms", "malajski");
    aMap.put ("mt", "malte\u0161ki");
    aMap.put ("mua", "mundang");
    aMap.put ("mul", "Vi\u0161e jezika");
    aMap.put ("mus", "Kri\u0161ki");
    aMap.put ("mwl", "Mirande\u0161ki");
    aMap.put ("mwr", "Marvari");
    aMap.put ("my", "burmanski");
    aMap.put ("myv", "Erzija");
    aMap.put ("na", "Nauru");
    aMap.put ("nap", "Neapolitanski");
    aMap.put ("naq", "nama");
    aMap.put ("nb", "norve\u0161ki bokmal");
    aMap.put ("nd", "severni ndebele");
    aMap.put ("nds", "Niski nema\u010dki");
    aMap.put ("ne", "nepalski");
    aMap.put ("new", "Nevari");
    aMap.put ("ng", "Ndonga");
    aMap.put ("nia", "Nias");
    aMap.put ("niu", "Niuean");
    aMap.put ("nl", "holandski");
    aMap.put ("nmg", "kvasio");
    aMap.put ("nn", "norve\u0161ki ninorsk");
    aMap.put ("no", "Norve\u0161ki");
    aMap.put ("nog", "Nogai");
    aMap.put ("non", "Stari norski");
    aMap.put ("nqo", "N\u2019ko");
    aMap.put ("nr", "Ju\u017eni ndebele");
    aMap.put ("nso", "Severni soto");
    aMap.put ("nus", "nuer");
    aMap.put ("nv", "Navaho");
    aMap.put ("nwc", "Klasi\u010dni nevari");
    aMap.put ("ny", "Njanja");
    aMap.put ("nym", "Njamvezi");
    aMap.put ("nyn", "njankole");
    aMap.put ("nyo", "Njoro");
    aMap.put ("nzi", "Nzima");
    aMap.put ("oc", "Provansalski");
    aMap.put ("oj", "Ojibva");
    aMap.put ("om", "oromo");
    aMap.put ("or", "orija");
    aMap.put ("os", "Osetski");
    aMap.put ("osa", "Osage");
    aMap.put ("ota", "Otomanski turski");
    aMap.put ("pa", "pand\u017eabi");
    aMap.put ("pag", "Pangasinski");
    aMap.put ("pal", "Pahlavi");
    aMap.put ("pam", "Pampanga");
    aMap.put ("pap", "Papiamento");
    aMap.put ("pau", "Palauanski");
    aMap.put ("peo", "Staropersijski");
    aMap.put ("phn", "Feni\u010danski");
    aMap.put ("pi", "Pali");
    aMap.put ("pl", "poljski");
    aMap.put ("pon", "Ponpejski");
    aMap.put ("pro", "Staroprovansalski");
    aMap.put ("ps", "pa\u0161tunski");
    aMap.put ("pt", "portugalski");
    aMap.put ("qu", "ke\u010dua");
    aMap.put ("quc", "k\u2019i\u010de");
    aMap.put ("raj", "Ra\u0111astani");
    aMap.put ("rap", "Rapanui");
    aMap.put ("rar", "Rarotongan");
    aMap.put ("rm", "reto-romanski");
    aMap.put ("rn", "rundi");
    aMap.put ("ro", "rumunski");
    aMap.put ("rof", "rombo");
    aMap.put ("rom", "Romani");
    aMap.put ("root", "Rut");
    aMap.put ("ru", "ruski");
    aMap.put ("rup", "Aromanijski");
    aMap.put ("rw", "kinjaruanda");
    aMap.put ("rwk", "rua");
    aMap.put ("sa", "sanskrit");
    aMap.put ("sad", "Sandave");
    aMap.put ("sah", "Jakut");
    aMap.put ("sam", "Samaritanski aramejski");
    aMap.put ("saq", "samburu");
    aMap.put ("sas", "Sasak");
    aMap.put ("sat", "Santali");
    aMap.put ("sbp", "sangu");
    aMap.put ("sc", "Sardinjaski");
    aMap.put ("scn", "Sicilijanski");
    aMap.put ("sco", "\u0160kotski");
    aMap.put ("sd", "sindi");
    aMap.put ("se", "severni sami");
    aMap.put ("seh", "sena");
    aMap.put ("sel", "Selkap");
    aMap.put ("ses", "kojraboro seni");
    aMap.put ("sg", "sango");
    aMap.put ("sga", "Staroirski");
    aMap.put ("sh", "Srpskohrvatski");
    aMap.put ("shi", "ta\u0161elhit");
    aMap.put ("shn", "\u0160an");
    aMap.put ("si", "sinhalski");
    aMap.put ("sid", "Sidamo");
    aMap.put ("sk", "slova\u010dki");
    aMap.put ("sl", "slovena\u010dki");
    aMap.put ("sm", "Samoanski");
    aMap.put ("sma", "ju\u017eni sami");
    aMap.put ("smj", "lule sami");
    aMap.put ("smn", "inari sami");
    aMap.put ("sms", "skolt sami");
    aMap.put ("sn", "\u0161ona");
    aMap.put ("snk", "Soninke");
    aMap.put ("so", "somalski");
    aMap.put ("sog", "Sod\u017eijenski");
    aMap.put ("sq", "albanski");
    aMap.put ("sr", "srpski");
    aMap.put ("srn", "Srananski tongo");
    aMap.put ("srr", "Serer");
    aMap.put ("ss", "Svati");
    aMap.put ("st", "Sesoto");
    aMap.put ("su", "sudanski");
    aMap.put ("suk", "Sukuma");
    aMap.put ("sus", "Susu");
    aMap.put ("sux", "Sumerski");
    aMap.put ("sv", "\u0161vedski");
    aMap.put ("sw", "svahili");
    aMap.put ("swb", "Komorski");
    aMap.put ("swc", "kongo svahili");
    aMap.put ("syc", "Klasi\u010dni sirijski");
    aMap.put ("syr", "Sirijski");
    aMap.put ("ta", "tamilski");
    aMap.put ("te", "telugu");
    aMap.put ("tem", "Timne");
    aMap.put ("teo", "teso");
    aMap.put ("ter", "Tereno");
    aMap.put ("tet", "Tetum");
    aMap.put ("tg", "Ta\u0111ik");
    aMap.put ("th", "tajlandski");
    aMap.put ("ti", "tigrinja");
    aMap.put ("tig", "Tigre");
    aMap.put ("tiv", "Tiv");
    aMap.put ("tk", "turkmenski");
    aMap.put ("tkl", "Tokelau");
    aMap.put ("tl", "Tagalski");
    aMap.put ("tlh", "Klingonski");
    aMap.put ("tli", "Tlingit");
    aMap.put ("tmh", "Tama\u0161ek");
    aMap.put ("tn", "Tsvana");
    aMap.put ("to", "tonga");
    aMap.put ("tog", "Njasa tonga");
    aMap.put ("tpi", "Tok Pisin");
    aMap.put ("tr", "turski");
    aMap.put ("ts", "Tsonga");
    aMap.put ("tsi", "Tsim\u0161ian");
    aMap.put ("tt", "tatarski");
    aMap.put ("tum", "Tumbuka");
    aMap.put ("tvl", "Tuvalu");
    aMap.put ("tw", "Tvi");
    aMap.put ("twq", "tasavak");
    aMap.put ("ty", "Tahi\u0107anski");
    aMap.put ("tyv", "Tuvinijski");
    aMap.put ("tzm", "centralnoatlaski tamazigt");
    aMap.put ("udm", "Udmurt");
    aMap.put ("ug", "ujgurski");
    aMap.put ("uga", "Ugaritski");
    aMap.put ("uk", "ukrajinski");
    aMap.put ("umb", "Umbundu");
    aMap.put ("und", "nepoznat jezik");
    aMap.put ("ur", "urdu");
    aMap.put ("uz", "uzbe\u010dki");
    aMap.put ("vai", "vai");
    aMap.put ("ve", "Venda");
    aMap.put ("vi", "vijetnamski");
    aMap.put ("vo", "Volapuk");
    aMap.put ("vot", "Votski");
    aMap.put ("vun", "vund\u017eo");
    aMap.put ("wa", "Valun");
    aMap.put ("wal", "Valamo");
    aMap.put ("war", "Varaj");
    aMap.put ("was", "Va\u0161o");
    aMap.put ("wo", "volof");
    aMap.put ("xal", "Kalmik");
    aMap.put ("xh", "kosa");
    aMap.put ("xog", "soga");
    aMap.put ("yao", "Jao");
    aMap.put ("yap", "Jape\u0161ki");
    aMap.put ("yi", "Jidi\u0161");
    aMap.put ("yo", "joruba");
    aMap.put ("yue", "Kantonski");
    aMap.put ("za", "\u017duang");
    aMap.put ("zap", "Zapote\u010dki");
    aMap.put ("zbl", "Blisimboli");
    aMap.put ("zen", "Zenaga");
    aMap.put ("zgh", "standardni marokanski tamazigt");
    aMap.put ("zh", "kineski");
    aMap.put ("zu", "zulu");
    aMap.put ("zun", "Zuni");
    aMap.put ("zxx", "bez lingvisti\u010dkog sadr\u017eaja");
    aMap.put ("zza", "Zaza");
    // To display SH in its own locale (overwrite "sh" value with "sr" value)
    aMap.put ("sh", "srpski");
    return aMap;
  }

  private static final Map <String, String> DISPLAY_COUNTRIES = _createDisplayCountries ();
  private static final Map <String, String> DISPLAY_LANGUAGES = _createDisplayLanguages ();

  public void checkLocaleSupported (final Locale aLocale)
  {
    if (aLocale == null)
      throw new NullPointerException ("locale must not be null");

    final Locale [] aSupportedLocales = getAllSupportedLocales ();

    // exact search
    for (final Locale aSupported : aSupportedLocales)
      if (aLocale.equals (aSupported))
        return;

    // relaxed search (seems to be necessary, but might also be a trouble
    // spot?)
    for (final Locale aSupported : aSupportedLocales)
      if (aLocale.getLanguage ().equals (aSupported.getLanguage ()))
        return;

    throw new IllegalArgumentException ("Unsupported locale '" +
                                        aLocale +
                                        "'. Valid loacles are: " +
                                        Arrays.toString (aSupportedLocales));
  }

  public Locale [] getAllSupportedLocales ()
  {
    return Arrays.copyOf (SUPPORTED_LOCALES, SUPPORTED_LOCALES.length);
  }

  public String getDisplayCountry (final String sCountryCode)
  {
    return DISPLAY_COUNTRIES.get (sCountryCode);
  }

  public String getDisplayLanguage (final String sLanguageCode)
  {
    return DISPLAY_LANGUAGES.get (sLanguageCode);
  }

  public String getCurrencyFormat ()
  {
    return CURRENCY_FORMAT;
  }

  public String getCurrencySymbol ()
  {
    return CURRENCY_SYMBOL;
  }

  public String getDateFull ()
  {
    return DATE_FULL;
  }

  public String getDateLong ()
  {
    return DATE_LONG;
  }

  public String getDateMedium ()
  {
    return DATE_MEDIUM;
  }

  public String getDateShort ()
  {
    return DATE_SHORT;
  }

  public char getDecimalSeparator ()
  {
    return DECIMAL_SEPARATOR;
  }

  public char getDigit ()
  {
    return DIGIT;
  }

  public String [] getEras ()
  {
    return Arrays.copyOf (ERAS, ERAS.length);
  }

  public String getExponentSeparator ()
  {
    return EXPONENT_SEPARATOR;
  }

  public char getGroupingSeparator ()
  {
    return GROUPING_SEPARATOR;
  }

  public String getInfinity ()
  {
    return INFINITY;
  }

  public String getIntegerFormat ()
  {
    return INTEGER_FORMAT;
  }

  public char getMinusSign ()
  {
    return MINUS_SIGN;
  }

  public String [] getMonths ()
  {
    return Arrays.copyOf (MONTHS, MONTHS.length);
  }

  public String getNaN ()
  {
    return NAN;
  }

  public String getNumberFormat ()
  {
    return NUMBER_FORMAT;
  }

  public String getPatternChars ()
  {
    return PATTERN_CHARS;
  }

  public char getPatternSeparator ()
  {
    return PATTERN_SEPARATOR;
  }

  public char getPercent ()
  {
    return PERCENT;
  }

  public String getPercentFormat ()
  {
    return PERCENT_FORMAT;
  }

  public char getPerMille ()
  {
    return PER_MILLE;
  }

  public String [] getShortMonths ()
  {
    return Arrays.copyOf (SHORT_MONTHS, SHORT_MONTHS.length);
  }

  public String [] getShortWeekdays ()
  {
    return Arrays.copyOf (SHORT_WEEKDAYS, SHORT_WEEKDAYS.length);
  }

  public String getTimeFull ()
  {
    return TIME_FULL;
  }

  public String getTimeLong ()
  {
    return TIME_LONG;
  }

  public String getTimeMedium ()
  {
    return TIME_MEDIUM;
  }

  public String getTimeShort ()
  {
    return TIME_SHORT;
  }

  public String [] getWeekdays ()
  {
    return Arrays.copyOf (WEEKDAYS, WEEKDAYS.length);
  }

  public char getZeroDigit ()
  {
    return ZERO_DIGIT;
  }

  public Collator getCollator ()
  {
    // TODO: shouldn't rely on HR locale
    return Collator.getInstance (HR_LOCALE);
  }
}
