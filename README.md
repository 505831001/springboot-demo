# springboot-demo

--
<!-- JS 部分 -->

<!-- JS 作用 -->
<!-- 网页特效(监听用户的一些行为让网页作出对应的反馈) -->
<!-- 表单验证(针对表单数据的合法性进行判断) -->
<!-- 数据交互(获取后台的数据渲染到前端) -->
<!-- 服务端编程(node.js) -->
<!-- JS 组成 -->
<!-- ECMAScript: 规定了js基础语法核心知识。比如：变量、分支语句、循环语句、对象等等。 -->
<!-- Web APIs DOM: 操作文档。比如对页面元素进行移动、大小、添加删除等操作。 -->
<!-- Web APIs BOM: 操作浏览器。比如页面弹窗，检测窗口宽度、存储数据到浏览器等等。 -->
<!-- JS 用法 -->
<!-- 使用 内联的 JavaScript 函数。 -->
<!-- 使用 <head>或者<body>中的 JavaScript 函数。 -->
<!-- 使用 外部的 JavaScript 函数。 -->
<!-- JS 注释 -->
<!-- 使用 //    开头的单行注释。快捷键：Ctrl + /。 -->
<!-- 使用 /* */ 包裹的多行注释。快捷键：Shift + Ctrl + A。 -->
<!-- JS 结束符 -->
<!-- 使用 分号（;）为结束符。换行为默认结束符。 -->
<!-- JS 字面量 -->
<!-- 使用 3.14 为数字字面量。 -->
<!-- 使用 双引号"白马王子" 单引号'黑马攻城狮' 为字符串字面量。 -->
<!-- 使用 大括号对象 {} 中括号数组 [] 尖括号标签<> 等等字面量。 -->
<!-- JS 输入/输出 -->
<!-- 使用 window.prompt()   方法显示可提示用户输入的对话框。 -->
<!-- 使用 element.innerHTML 属性写入到 HTML 元素。 -->
<!-- 使用 window.alert()    方法弹出警告框。 -->
<!-- 使用 document.write()  方法将内容写到 HTML 文档中。 -->
<!-- 使用 console.log()     方法写入到浏览器的控制台。 -->
<!-- JS 变量 -->
<!-- 使用 var   关键字声明的变量。在函数外声明作用域是全局变量，在函数内声明作用域是局部变量。变量必须以字母开头，变量也能以（$）和（_）符号开头，变量名称对大小写敏感。 -->
<!-- 使用 let   关键字定义的限定范围内作用域的变量。只在 let 命令所在的代码块内有效。 -->
<!-- 使用 const 关键字来定义一个常量。一旦声明，常量的值就不能改变。 -->
<!-- JS 数据类型 -->
<!-- 使用 number    基础数据类型赋值的是数字类型。JS 中的正数、负数、小数等等统一称为数字类型。 -->
<!-- 使用 string    基础数据类型赋值的是字符串类型。通过单引号（''）、双引号（""）或反引号（``）包裹的数据都叫字符串类型。 -->
<!-- 使用 boolean   基础数据类型赋值的是布尔类型。布尔（逻辑）类型只能有两个值真（true）或假（false）。 -->
<!-- 使用 undefined 基础数据类型返回值的是未定义类型。只声明变量，不赋值的情况下，变量的默认值为 undefined 这个值表示变量不含有值。 -->
<!-- 使用 null      基础数据类型返回值的是空值类型。可以通过将变量的值设置为 null 来清空变量。 -->
<!-- 使用 object    引用数据类型返回的值是对象。对象由花括号（{}）分隔，对象属性以名称和值对的形式（Key:Value）来定义，属性之间由逗号（,）分隔。 -->
<!-- 使用 array     引用数据类型返回的值是数组。数组由中括号（[]）分隔，每个值之间由逗号（,）分隔。返回也是对象：object。 -->
<!-- 使用 function  引用数据类型返回的值是函数。函数就是包裹在花括号中的代码块，前面使用了关键词 function。 -->
<!-- JS 运算符 -->
<!-- 使用 + - * / %            符号来表示数学运算符（算术运算符），主要包括加、减、乘、除、取余（求模）。 -->
<!-- 使用 = += -= *= /= %=     符号来表示赋值运算符，对变量进行赋值的运算符。 -->
<!-- 使用 > < >= <= == === !== 符号来表示比较运算符，比较两个数据大小、是否相等。 -->
<!-- 使用 i++ ++i i-- --i      符号来表示一元运算符，能够使用一元运算符做自增（自减）运算。 -->
<!-- 使用 && ||                符号来表示逻辑运算符，逻辑运算符用来解决多重条件判断。 -->
<!-- JS 分支/循环 -->
<!-- 使用 if (condition) { }                                   语句只有当指定条件为 true 时，使用该语句来执行代码。 -->
<!-- 使用 if (condition) { } else { }                          语句当条件为 true 时执行代码，当条件为 false 时执行其他代码。 -->
<!-- 使用 if (condition) { } else if (condition) else {}       语句使用该语句来选择多个代码块之一来执行。 -->
<!-- 使用 switch (key) {case value: break; default: break;}    语句用于基于不同的条件来执行不同的动作。 -->
<!-- 使用 while (condition) { }                                语句只要指定条件为 true，循环就可以一直执行代码块。 -->
<!-- 使用 do { } while (condition);                            语句循环会在检查条件是否为真之前执行一次代码块，然后如果条件为真的话，就会重复这个循环。 -->
<!-- 使用 for (let i = 0; i < array.length; i++) { array[i]; } 语句循环代码块一定的次数。 -->
<!-- 使用 array.forEach(element => { });                       语句循环代码块一定的次数。 -->
<!-- 使用 for (const key in object) { object[key]; }           语句循环遍历对象的属性。 -->
<!-- 使用 for (const iterator of object) { }                   语句循环遍历对象的属性。 -->
<!-- JS 数组 -->
<!-- 使用 array = new Array();                                 语句创建数组，基于 new 关键字。数组是一种可以按顺序保存数据的数据类型。 -->
<!-- 使用 array = [];                                          语句创建数组，基于中括号 []。数组是一种可以按顺序保存数据的数据类型。 -->
<!-- JS 操作符 -->
<!-- 使用 typeof                                               操作符来检测变量的数据类型。 -->
<!-- 使用 typeof                                               操作符来检测 null 是一个只有一个值的特殊类型。表示一个空对象引用。会返回 object。 -->
<!-- 使用 typeof                                               操作检测一个没有设置值的变量。会返回 undefined。 -->
<!-- JS 类型转换 -->
<!-- 隐式转换：某些运算符被执行时，系统内部自动将数据类型进行转换，这种转换称为隐式转换。 -->
<!-- 显式转换：为了避免因隐式转换带来的问题，通常根逻辑需要对数据进行显示转换。 -->
<!-- 使用 Number(x)                                            方法将字符串转换成数字类型。 -->
<!-- 使用 parseInt(x)                                          方法将字符串转换成数字类型。 -->
<!-- 使用 parseFloat(x)                                        方法将字符串转换成数字类型。 -->
<!-- 使用 String(x)                                            方法将其他类型转换成字符串类型。 -->
<!-- 使用 x.toString()                                         方法将其他类型转换成字符串类型。 -->
<!-- JS 正则表达式 -->
<!-- 语法 /正则表达式主体/修饰符(可选)                         第一步：语法。使用 /正则表达式主体/修饰符(可选) 定义规则，正则表达式使用单个字符串来描述、匹配一系列符合某个句法规则的字符串搜索模式。 -->
<!-- 使用 RegExp.test()                                        第二步：使用。使用 RegExp对象test() 方法用于检测一个字符串是否匹配某个模式，如果字符串中含有匹配的文本，则返回true，否则返回false。 -->
<!-- 使用 s.search(/RegExp/i)                                  正则表达式搜索 RegExp 字符串，且不区分大小写。 -->
<!-- 使用 s.replace(/RegExp/i, "RegularExpression")            正则表达式且不区分大小写将字符串中的 RegExp 替换为 RegularExpression。 -->
<!-- 使用 i                                                    修饰符执行对大小写不敏感的匹配。 -->
<!-- 使用 g                                                    修饰符执行全局匹配（查找所有匹配而非在找到第一个匹配后停止）。 -->
<!-- 使用 m                                                    修饰符执行多行匹配。 -->
<!-- 使用 [abc]                                                表达式查找方括号之间的任何字符。 -->
<!-- 使用 [0-9]                                                表达式查找任何从 0 至 9 的数字。 -->
<!-- 使用 (x|y)                                                表达式查找任何以 | 分隔的选项。 -->
<!-- 使用 \d                                                   元字符查找数字。 -->
<!-- 使用 \s                                                   元字符查找空白字符。 -->
<!-- 使用 \b                                                   元字符匹配单词边界。 -->
<!-- 使用 \uxxxx                                               元字符查找以十六进制数 xxxx 规定的 Unicode 字符。 -->
<!-- 使用 n+                                                   量词匹配任何包含至少一个 n 的字符串。 -->
<!-- 使用 n*                                                   量词匹配任何包含零个或多个 n 的字符串。 -->
<!-- 使用 n?                                                   量词匹配任何包含零个或一个 n 的字符串。 -->
<!-- 使用 RegExp                                               对象是一个预定义了属性和方法的正则表达式对象。 -->
<!-- 使用 test()                                               方法用于检测一个字符串是否匹配某个模式，如果字符串中含有匹配的文本，则返回 true，否则返回 false。 -->
<!-- 使用 exec()                                               方法用于检索字符串中的正则表达式的匹配。该函数返回一个数组，其中存放匹配的结果。如果未找到匹配，则返回值为 null。 -->
<!-- JS 异常 -->
<!-- 使用 try                                                  语句允许我们定义在执行时进行错误测试的代码块。 -->
<!-- 使用 catch                                                语句允许我们定义当 try 代码块发生错误时，所执行的代码块。 -->
<!-- 使用 finally                                              语句在 try 和 catch 语句之后，无论是否有触发异常，该语句都会执行。 -->
<!-- 使用 throw                                                语句创建自定义错误。或者叫做创建或者抛出异常（Exception）。 -->
<!-- JS 关键字 -->
<!-- 使用 break                                                关键字 -->
<!-- 使用 continue                                             关键字 -->
<!-- 使用 return                                               关键字 -->
<!-- 使用 typeof                                               关键字 -->
<!-- 使用 this                                                 关键字 -->
<!-- 使用 var                                                  关键字 -->
<!-- 使用 let                                                  关键字 -->
<!-- 使用 const                                                关键字 -->
<!-- 使用 void                                                 关键字 -->
<!-- JS 异步编程 -->
<!-- 同步 sync                                                 相对的概念。 -->
<!-- 异步 async                                                相对的概念。 -->
<!-- 使用 Promise                                              对象是一个 ECMAScript 6 提供的类，目的是更加优雅地书写复杂的异步任务。 -->
<!-- 使用 new Promise(function(resolve, reject){setTimeout(function(){}, 1000);})     异步函数是 ECMAScript 2017 (ECMA-262) 标准的规范，几乎被所有浏览器所支持，除了 Internet Explorer。 -->
<!-- JS 函数 -->
<!-- 使用 function functionname(parameters) { return parameters; }                    声明函数。函数声明后不会立即执行，会在我们需要的时候调用到。 -->
<!-- 使用 x = function (a, b) { return a * b; };                                      匿名函数（函数表达式）。函数存储在变量中，不需要函数名称，通常通过变量名来调用。 -->
<!-- 使用 new Function("a", "b", "return a * b");                                     构造函数。函数同样可以通过内置的 JavaScript 函数构造器 Function() 定义。 -->
<!-- 使用 (function () { var x = "callback"; })();                                    自调用函数（立即执行函数）。函数表达式可以"自调用"，自调用表达式会自动调用。如果表达式后面紧跟 () 则会自动调用。不能自调用声明的函数。 -->
<!-- 使用 (function () { var x = "callback"; }());                                    自调用函数（立即执行函数）。函数实际上是一个匿名自我调用的函数（没有函数名）。 -->
<!-- 使用 (parameters) => { return x * y; }                                           箭头函数。ES6 新增了箭头函数。箭头函数表达式的语法比普通函数表达式更简洁。 -->
<!-- 使用 function callback() { return a * b; } window.setInterval(callback(), 1000); 回调函数。函数当作参数传递给另外一个函数，这个函数就是回调函数。 -->
<!-- 使用 window.addEventListener('click', function () { return a * b; });            回调函数。函数当作参数传递给另外一个函数，这个函数就是回调函数。 -->
<!-- 使用 window.addEventListener('change', functionname())                           调用函数。会立即执行函数，无需等待事件触发。立即执行函数的一种。 -->
<!-- 使用 window.addEventListener('change', functionname)                             调用函数。需要等待事件触发之后调用函数。因此书写时不需要加括号。 -->
<!-- JS 闭包 -->
<!-- 使用 x = (function () {var i = 0; return function () {return i += 1;}})();       闭包是一种保护私有变量的机制，在函数执行时形成私有的作用域，保护里面的私有变量不受外界干扰。 -->
<!-- 使用 x = (function () {var i = 0; return function () {return i += 1;}})();       x 变量可以作为一个函数使用。它 function () {return i += 1;} 可以访问函数上一层作用域的计数器。这个叫作 JavaScript 闭包。它使得函数拥有私有变量变成可能。 -->
<!-- JS 类 -->
<!-- 使用 class Parent { property: "value", method: function () { } }                 使用 class 关键字来创建一个类，类是用于创建对象的模板。类体在一对大括号 {} 中，在大括号 {} 中定义类成员的位置，如方法或构造函数。每个类中包含了一个特殊的方法 constructor()，它是类的构造函数。这种方法用于创建和初始化一个由 class 创建的对象。 -->
<!-- 使用 new Parent()                                                                使用 new 关键字来创建对象。创建对象时会自动调用构造函数方法 constructor()。 -->
<!-- 使用 class Children extends Parent { }                                           使用 extends 关键字继承类。这个已有的类称为基类（父类），新建的类称为派生类（子类）。 -->
<!-- 使用 static method() { }                                                         使用 static 关键字修改的方法是静态方法，又叫类方法，属于类的，但不属于对象。通过（类名.方法名）调用静态方法。静态方法不能在对象上调用，只能在类中调用。 -->
<!-- JS 对象 -->
<!-- 使用 Console 对象提供了浏览器控制台调试的接口。在不同浏览器上它的工作方式可能不一样，但通常都会提供一套共性的功能。 -->




--
<!-- DOM 部分 -->

<!-- DOM 获取元素 -->
<!-- 通过 document.getElementById()         方法通过 ID 找到 HTML 元素。 -->
<!-- 通过 document.getElementsByTagName()   方法通过 标签名 找到 HTML 元素。 -->
<!-- 通过 document.getElementsByClassName() 方法通过 类名 找到 HTML 元素。 -->
<!-- 通过 document.querySelector()          方法返回文档中与指定的选择器匹配的第一个元素 Element 节点。 -->
<!-- 通过 document.querySelectorAll()       方法返回包含文档中与指定的选择器匹配的所有元素 NodeList 节点的列表。 -->
<!-- DOM 修改元素内容 -->
<!-- 通过 document.write()                  方法可向文档写入文本内容，可以是 HTML 代码。 -->
<!-- 通过 element.innerText                 属性设置或者返回元素的内容。 -->
<!-- 通过 element.innerHTML                 属性设置或获取 HTML 语法表示的元素的后代。 -->
<!-- DOM 修改元素属性 -->
<!-- 通过 element.href                      属性设置或者返回元素新赋值属性。 -->
<!-- 通过 element.title                     属性设置或者返回元素新赋值属性。 -->
<!-- 通过 element.src                       属性设置或者返回元素新赋值属性。 -->
<!-- 通过 element.className                 属性设置或者返回元素新赋值属性。 -->
<!-- 通过 element.disabled                  属性规定应该启用或者禁用的 input 元素。 -->
<!-- 通过 element.checked                   属性规定在页面加载时应该被预先选定的 input 元素。只针对 type="checkbox" 或者 type="radio"。 -->
<!-- 通过 element.selected                  属性规定选项（在首次显示在列表中时）表现为选中状态。 -->
<!-- DOM 修改元素样式 -->
<!-- 通过 element.style.styleProperties     属性设置或者返回元素的样式属性。 -->
<!-- DOM 定时器 -->
<!-- 通过 window.setInterval(function, timeout)        方法按照指定的周期（毫秒）来无限循环调用，调用函数或计算表达式。 -->
<!-- 通过 window.clearInterval(timer)                  方法取消由定时器设置的定时任务。 -->
<!-- 通过 window.setTimeout(function, timeout)         方法在指定的毫秒数后仅此调用一次，调用函数或计算表达式。 -->
<!-- 通过 window.clearTimeout(timer)                   方法取消由定时器设置的定时任务。 -->
<!-- DOM 事件 -->
<!-- 使用 onclick = function() { }                                             传统(on)注册事件（L0）。同一个对象，后面注册的事件会覆盖前面注册（同一个事件）。 -->
<!-- 使用 window.addEventListener('event',  function () {})                    事件监听注册事件（L2）。后面注册的事件不会覆盖前面注册的事件（同一个事件）。 -->
<!-- 使用 window.addEventListener('load',   function () {})                    事件监听注册事件（L2）。加载事件。加载外部资源（如图片、CSS和JavaScript等）加载完毕时触发的事件。 -->
<!-- 使用 window.addEventListener('scroll', function () {})                    事件监听注册事件（L2）。滚动事件。页面进行滚动的时候触发的事件。监听某个元素的内部滚动直接给某个元素加即可。 -->
<!-- 使用 onclick = null                                                       传统方式删除事件。 -->
<!-- 使用 removeEventListener('click', func)                                   监听方式删除事件。非匿名函数，必须命名进行回调。 -->
<!-- DOM 事件流 -->
<!-- 使用 window.addEventListener('event', function() {}, function callback()) 此方法若传入 false 代表冒泡阶段触发，默认就是 false。第三个参数传入 true 代表是捕获阶段触发（很少使用）。 -->
<!-- 使用 window.stopPropagation()                                             此方法可以阻断事件流动传播，不光在冒泡阶段有效，捕获阶段也有。当触发事件时，会经历两个阶段，分别是捕获阶段、冒泡阶段。 -->
<!-- DOM 事件委托 -->
<!-- 使用 window.target(Event.target)                                          此方法可以获得真正触发事件的元素。事件委托其实是利用事件冒泡的特点，给父元素添加事件，子元素（target）可以触发。 -->
<!-- DOM 函数 -->
<!-- 使用 function functionname(parameters) { return parameters; }             声明函数。 -->
<!-- 使用 function (a, b) {return a * b};                                      匿名函数。 -->
<!-- 使用 new Function("a", "b", "return a * b");                              构造函数。 -->
<!-- 使用 (function () { var x = "callback"; })();                             自调用函数（立即执行函数）。 -->
<!-- 使用 (function () { var x = "callback"; }());                             自调用函数（立即执行函数）。 -->
<!-- 使用 (parameters) => { return x * y; }                                    箭头函数。 -->
<!-- 使用 function callback() { return '回调函数'; } window.setInterval(callback(), 1000) 回调函数。函数当作参数传递给另外一个函数，这个函数就是回调函数。 -->
<!-- 使用 window.addEventListener('click', function () { return '回调函数'; })            回调函数。函数当作参数传递给另外一个函数，这个函数就是回调函数。 -->
<!-- 使用 window.addEventListener('change', functionname())                    调用函数。会立即执行函数，无需等待事件触发。立即执行函数的一种。 -->
<!-- 使用 window.addEventListener('change', functionname)                      调用函数。需要等待事件触发之后调用函数。因此书写时不需要加括号。 -->
<!-- DOM 节点操作 -->
<!-- 使用 element.parentNode                    属性父节点查找。返回最近一级的父节点找不到返回为 null。 -->
<!-- 使用 element.childNodes                    属性获得所有子节点，包括文本节点（空格、换行）注释节点等。 -->
<!-- 使用 element.children                      属性仅获得所有元素节点。返回的还是一个伪数组。 -->
<!-- 使用 element.nextElementSibling            属性查找下一个兄弟节点。 -->
<!-- 使用 element.previousElementSibling        属性查找上一个兄弟节点。 -->
<!-- 使用 document.createElement('elementName') 属性新建一个新的网页元素，再添加到网页内，一般先创建节点，然后插入节点。 -->
<!-- 使用 element.appendChild('elementName')    属性要想在界面看到，还得插入到某个父元素中。插入到父元素的最后一个子元素。 -->
<!-- 使用 element.cloneNode(boolean)            属性会克隆出一个跟原标签一样的元素，括号内传入布尔值。若为true，则代表克隆时会包含后代节点一起克隆，若为false，则代表克隆时不包含后代节点，默认false。 -->
<!-- 使用 element.removeChild('elementName')    属性如不存在父子关系则删除不成功。 -->
<!-- DOM 节点滚动 -->
<!-- 使用 Element.scrollWidth                   返回类型为：Number，表示元素的滚动视图宽度。 -->
<!-- 使用 Element.scrollHeight                  返回类型为：Number，表示元素的滚动视图高度。 -->
<!-- 使用 Element.scrollLeft                    检测元素的内容左右和上下滚动的距离（被卷去的左侧scrollLeft），返回值不带单位。 -->
<!-- 使用 Element.scrollTop                     检测元素的内容左右和上下滚动的距离（被卷去的头部scrollTop），返回值不带单位。 -->
<!-- DOM 节点位置 -->
<!-- 使用 element.offsetWidth                   返回元素的宽度，包括边框（border）和内边距（padding），但不包含外边距（margin）。 -->
<!-- 使用 element.offsetHeight                  返回任何一个元素的高度包括边框（border）和内边距（padding），但不包含外边距（margin）。 -->
<!-- 使用 element.offsetLeft                    检测元素相对于父级元素的左右偏移量位置，返回值不带单位。 -->
<!-- 使用 element.offsetTop                     检测元素相对于父级元素的上下偏移量位置，返回值不带单位。 -->
<!-- DOM 节点大小 -->
<!-- 使用 Element.clientWidth                   检测元素的内容宽度（可见的区域大小（不包含滚动条和不包含超出的部分）），返回值不带单位。 -->
<!-- 使用 Element.clientHeight                  检测元素的内容高度（可见的区域大小（不包含滚动条和不包含超出的部分）），返回值不带单位。 -->
<!-- 使用 Element.clientLeft                    只读，返回 Number 表示该元素距离它左边界的宽度。 -->
<!-- 使用 Element.clientTop                     只读，返回 Number 表示该元素距离它上边界的高度。 -->
<!-- DOM 事件对象 -->
<!-- 使用 o.addEventListener('event', function () {}) 事件监听对象。 -->
<!-- 使用 MouseEvent.clientX                    鼠标指针在点击元素（DOM）中的 X 坐标。 -->
<!-- 使用 MouseEvent.clientY                    鼠标指针在点击元素（DOM）中的 Y 坐标。 -->
<!-- 使用 MouseEvent.offsetX                    鼠标指针相对于目标节点内边位置的 X 坐标。 -->
<!-- 使用 MouseEvent.offsetY                    鼠标指针相对于目标节点内边位置的 Y 坐标。 -->
<!-- 使用 MouseEvent.pageX                      鼠标指针相对于整个文档的 X 坐标。 -->
<!-- 使用 MouseEvent.pageY                      鼠标指针相对于整个文档的 Y 坐标。 -->
<!-- 使用 MouseEvent.screenX                    鼠标指针相对于全局（屏幕）的 X 坐标。 -->
<!-- 使用 MouseEvent.screenY                    鼠标指针相对于全局（屏幕）的 Y 坐标。 -->
<!-- DOM 本地存储 -->
<!-- 使用 window.sessionStorage                 生命周期，约存储 05M 数据，只存在于页面打开与关闭之间。 -->
<!-- 使用 window.localStorage                   生命周期，约存储 20M 数据，永久存在。除非手动删除。还TMD可以同浏览器共享（多个窗口（页面））。 -->
<!-- BOM 对象 -->
<!-- Window.closed         非标准。只读这个属性指示当前窗口是否关闭。 -->
<!-- Window.console        只读返回 console 对象的引用，该对象提供了对浏览器调试控制台的访问。 -->
<!-- Window.document       只读返回对当前窗口所包含文档的引用。 -->
<!-- Window.frameElement   只读返回嵌入窗口的元素，如果未嵌入窗口，则返回 null。 -->
<!-- Window.frames         只读返回当前窗口中所有子窗体的数组。 -->
<!-- Window.fullScreen     此属性表示窗口是否以全屏显示。 -->
<!-- Window.history        只读返回一个对 history 对象的引用。 -->
<!-- Window.innerHeight    只读获得浏览器窗口的内容区域的高度，包含水平滚动条（如果有的话）。 -->
<!-- Window.innerWidth     只读获得浏览器窗口的内容区域的宽度，包含垂直滚动条（如果有的话）。 -->
<!-- Window.isSecureContext实验性 只读指出上下文环境是否能够使用安全上下文环境的特征。 -->
<!-- Window.length         只读返回窗口中的 frames 数量。参见 window.frames。 -->
<!-- Window.location       获取或者设置 window 对象的 location 定位属性，或者当前的 URL 地址。 -->
<!-- Window.locationbar    只读返回 locationbar 对象，其可视性可以在窗口中切换。 -->
<!-- Window.localStorage   只读返回用来存储只能在创建它的源下访问的数据的本地存储对象的引用。 -->
<!-- Window.menubar        只读返回菜单条对象，它的可视性可以在窗口中切换。 -->
<!-- Window.messageManager 返回窗口的 message manager 对象。 -->
<!-- Window.name           获取或者设置窗口的名称。 -->
<!-- Window.navigator      只读返回对 navigator 对象的引用。 -->
<!-- Window.opener         返回对打开当前窗口的那个窗口的引用。 -->
<!-- Window.outerHeight    只读返回浏览器窗口的外部高度。 -->
<!-- Window.outerWidth     只读返回浏览器窗口的外部宽度。 -->
<!-- Window.pageXOffset    只读window.scrollX的别名。 -->
<!-- Window.pageYOffset    只读window.scrollY的别名。 -->
<!-- Window.parent         只读返回当前窗口或子窗口的父窗口的引用。 -->




-- JavaScript 总结-- 



