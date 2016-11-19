/*
 * Copyright (C) 2011-2014 Philip Helger <philip[at]helger[dot]com>
 * All Rights Reserved
 *
 * This file is part of the LaMaCheck service.
 *
 * Proprietary and confidential.
 *
 * It can not be copied and/or distributed without the
 * express permission of Philip Helger.
 *
 * Unauthorized copying of this file, via any medium is
 * strictly prohibited.
 */
function LamaClass(){}
LamaClass.prototype = {
  viewLogin : function(ajaxUrl,vals,errorField) {
    $.ajax ({
      type: 'POST',
      url:ajaxUrl,
      data:vals,
      success:function(data){
        if (data.loggedin) {
          // reload the whole page because of too many changes
          location.reload ();
        }
        else
          $('#'+errorField).empty ().append (data.html);
      }
    });
  }
};

var Lama = window.Lama = new LamaClass();
