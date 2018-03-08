# Университет Итмо. Кафедра КТ
<h3 id="homework-9">Web Crawler</h3><ol><li>
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
        </li></ol><h3 id="homework-10">Offline Browser</h3><ol><li>
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
