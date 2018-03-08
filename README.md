# Университет Итмо. Кафедра КТ
<h3 id="homework-9">Домашнее задание 9. Web Crawler</h3><ol><li>
            Напишите Web Crawler, обходящий HTML-страницы
            на заданную глубину и вытаскивающий
            из них картинки.
        </li><li>
            Информация о HTML странице (класс <code>Page</code>:
            <ul><li><code>String url</code> &ndash;
                    URL страницы (идентификатор);
                </li><li><code>String title</code> &ndash;
                    заголовок страницы
                    (содержимое элемента <code>title</code>);
                </li><li><code>List&lt;Page&gt; links</code> &ndash;
                    ссылки (атрибут <code>href</code> элемента <code>a</code>),
                    в порядке появления на странице;
                </li><li><code>List&lt;Page&gt; backLinks</code> &ndash;
                    ссылки, ведущие на страницу;
                </li><li><code>List&lt;Image&gt; images</code> &ndash;
                    Картинки на странице (элемент <code>img</code>),
                    в порядке появления на странице.
                </li></ul></li><li>
            Информация о картинке (класс <code>Image</code>):
            <ul><li><code>String url</code> &ndash;
                    URL картинки (идентификатор);
                </li><li><code>String file</code> &ndash;
                    имя файла, в котором сохранена картинка;
                </li><li><code>List&lt;String&gt; pages</code> &ndash;
                    страницы, на которых встречается картинка.
                </li></ul></li><li>
            Интерфес Web Crawler:
            <pre>
public interface WebCrawler {
    Page crawl(String url, int depth);
}
            </pre></li><li>
            При загрузке на глубину два, должны быть загружены
            и проанализированы  переданная страница и
            страницы, на которые она ссылается.
        </li><li>
            Для загрузки страниц и картинок можно использовать
            метод <a href="https://docs.oracle.com/javase/8/docs/api/java/net/URL.html#openStream--">openStream</a>
            класса <a href="https://docs.oracle.com/javase/8/docs/api/java/net/URL.html">URL</a>.
        </li><li>
            Вы можете считать, что все страницы имеют кодировку UTF-8.
        </li></ol><h3 id="homework-10">Домашнее задание 10. Offline Browser</h3><ol><li>
            Напишите Offline Browser, обходящий HTML-страницы
            на заданную глубину и сохраняющий их для
            offline-просмотра.
        </li><li>
            Вместе с HTML-страницами должны быть загружены
            сопутствующие ресурсы:  картинки, скрипты и css-файлы.
            При сохранении не должны создаваться лишние
            копии ресурсов.
        </li><li>
            Ссылки на сохраненные страницы должны быть  изменены так,
            чтобы работать без подключения к Интернету.
            Ссылки на другие страницы должны остаться без изменений.
        </li><li>
            Вы можете считать, что все страницы имеют кодировку UTF-8.
        </li><li><em>Примечание</em>.
            В результате работы наивного offline-браузера, некоторые
            страницы (например, использующие динамическую загрузку
            скриптов и CSS) могут отображаться некорректно.
            Правильная загрузка таких сайтов не входит в
            данное домашнее задание.
        </li></ol></td><td id="sidebar"><div id="sidebar-head"><form method="get" action="https://www.google.com/search"><p><input type="hidden" name="sitesearch" value="kgeorgiy.info"></p><table><tr><td style="width:100%"><input style="width:100%" type="text" name="q" maxlength="255"></td><td><button type="submit" value="Search"><img alt="Search" src="/design/find.png" width="16" height="16"></button></td></tr></table></form></div><div id="sidebar-body"><h3><a href="#homework-1">Домашнее задание 1. Сумма чисел</a></h3><h3><a href="#homework-2">Домашнее задание 2. Реверс</a></h3><h3><a href="#homework-3">Домашнее задание 3. Сумма чисел в файле</a></h3><h3><a href="#homework-4">Домашнее задание 4. Статистика слов</a></h3><h3><a href="#homework-5">Домашнее задание 5. Быстрый реверс</a></h3><h3><a href="#homework-6">Домашнее задание 6. Статистика слов++</a></h3><h3><a href="#homework-7">Домашнее задание 7. Разметка</a></h3><h3><a href="#homework-8">Домашнее задание 8. Markdown to HTML</a></h3><h1><a href="#N65933">Заголовок первого уровня</a></h1><h2><a href="#N65935">Второго</a></h2><h3><a href="#N65937">Третьего ## уровня</a></h3><h4><a href="#N65939">Четвертого
# Все еще четвертого</a></h4>
