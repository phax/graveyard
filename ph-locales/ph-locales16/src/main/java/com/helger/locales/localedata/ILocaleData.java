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
package com.helger.locales.localedata;

import java.io.Serializable;
import java.text.Collator;
import java.util.Locale;

/**
 * This interface bundles all locale-specific settings to be used by providers
 * and other classes.
 */
public interface ILocaleData extends Serializable
{
  void checkLocaleSupported (Locale aLocale);

  Locale [] getAllSupportedLocales ();

  // CollatorProvider
  /**
   * @return CollatorProvider collator
   */
  Collator getCollator ();

  // DateFormatProvider
  /**
   * @return DateFormatProvider date full
   */
  String getDateFull ();

  /**
   * @return DateFormatProvider date long
   */
  String getDateLong ();

  /**
   * @return DateFormatProvider date medium
   */
  String getDateMedium ();

  /**
   * @return DateFormatProvider date short
   */
  String getDateShort ();

  /**
   * @return DateFormatProvider time full
   */
  String getTimeFull ();

  /**
   * @return DateFormatProvider time long
   */
  String getTimeLong ();

  /**
   * @return DateFormatProvider time medium
   */
  String getTimeMedium ();

  /**
   * @return DateFormatProvider time short
   */
  String getTimeShort ();

  // DateFormatSymbols
  /**
   * @return DateFormatSymbols eras
   */
  String [] getEras ();

  /**
   * @return DateFormatSymbols pattern chars
   */
  String getPatternChars ();

  /**
   * @return DateFormatSymbols months
   */
  String [] getMonths ();

  /**
   * @return DateFormatSymbols short months
   */
  String [] getShortMonths ();

  /**
   * @return DateFormatSymbols weekdays
   */
  String [] getWeekdays ();

  /**
   * @return DateFormatSymbols short weekdays
   */
  String [] getShortWeekdays ();

  // DecimalFormatSymbols
  /**
   * @return DecimalFormatSymbols decimal separator
   */
  char getDecimalSeparator ();

  /**
   * @return DecimalFormatSymbols grouping separator
   */
  char getGroupingSeparator ();

  /**
   * @return DecimalFormatSymbols pattern separator
   */
  char getPatternSeparator ();

  /**
   * @return DecimalFormatSymbols percent
   */
  char getPercent ();

  /**
   * @return DecimalFormatSymbols zero digit
   */
  char getZeroDigit ();

  /**
   * @return DecimalFormatSymbols digit
   */
  char getDigit ();

  /**
   * @return DecimalFormatSymbols minus sign
   */
  char getMinusSign ();

  /**
   * @return DecimalFormatSymbols exponent separator
   */
  String getExponentSeparator ();

  /**
   * @return DecimalFormatSymbols per mille
   */
  char getPerMille ();

  /**
   * @return DecimalFormatSymbols infinity
   */
  String getInfinity ();

  /**
   * @return DecimalFormatSymbols NaN
   */
  String getNaN ();

  /**
   * @return DecimalFormatSymbols Currency symbol
   */
  String getCurrencySymbol ();

  // LocaleNameProvider
  /**
   * @param sCountryCode
   *        Country code to get the display country from
   * @return LocaleNameProvider country display name
   */
  String getDisplayCountry (String sCountryCode);

  /**
   * @param sLanguageCode
   *        Language code to the the display language from
   * @return LocaleNameProvider language display name
   */
  String getDisplayLanguage (String sLanguageCode);

  // NumberFormatProvider
  /**
   * @return NumberFormatProvider currency format
   */
  String getCurrencyFormat ();

  /**
   * @return NumberFormatProvider integer format
   */
  String getIntegerFormat ();

  /**
   * @return NumberFormatProvider number format
   */
  String getNumberFormat ();

  /**
   * @return NumberFormatProvider percent format
   */
  String getPercentFormat ();
}
