/*******************************************************************************
 * Copyright (c) 2017, 2020 THALES GLOBAL SERVICES.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.common.application;

import java.util.HashMap;

import org.eclipse.core.runtime.Platform;

/**
 * This argument helper retrieve arguments given to the application. By default, it reads Platform.getApplicationArgs()
 * 
 * It be customized to load additional parameters from IApplicationContext if IApplicationContext is not the default one
 * provided by the Platform.
 */
public class ArgumentsHelper {

  private HashMap<String, String> values = null;

  private boolean defaultLoaded = false;

  private static ArgumentsHelper instance = new ArgumentsHelper(); //创建静态实例

  public static ArgumentsHelper getInstance() {
    return instance;
  }

  private void loadArguments() {
    if (!defaultLoaded) {
      defaultLoaded = true;
      loadArguments(Platform.getApplicationArgs()); //返回框架实现本身未使用的参数。
    }
  }

  public void loadArguments(String[] args) {
    if (values == null) {
      values = new HashMap<String, String>(); //创建hashmap
    }

    for (int i = 0; i < args.length; i++) {
      String arg = args[i].toLowerCase(); //换为小写

      if (arg.startsWith("-")) { //$NON-NLS-1$ 字符是否是以‘-’开始
        String value = i < args.length - 1 ? args[i + 1] : null; //value是下一个参数，最后一个参数没有
        if (value != null && !value.startsWith("-")) { //$NON-NLS-1$
          values.put(arg, value); //推入hashmap: [当前参数，下一个参数] 即一个选项 和 选项的值
        } else
          values.put(arg, null);
      }
    }
  }

  public String getString(String parameter) {
    loadArguments();
    String arg = parameter.toLowerCase();
    if (values.containsKey(arg)) {
      return values.get(arg); //根据选项返回值
    }
    return null;
  }

  public boolean hasParameter(String parameter) {
    loadArguments();
    String arg = parameter.toLowerCase();
    return values.containsKey(arg);
  }

}
