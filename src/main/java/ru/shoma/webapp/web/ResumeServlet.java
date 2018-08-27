package ru.shoma.webapp.web;

import ru.shoma.webapp.Config;
import ru.shoma.webapp.model.*;
import ru.shoma.webapp.storage.Storage;
import ru.shoma.webapp.util.DateUtil;
import ru.shoma.webapp.util.HtmlUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            storage = Config.getInstance().getStorage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");

        final boolean isCreate = (uuid == null || uuid.length() == 0);
        Resume r;
        if (isCreate) {
            r = new Resume(fullName);
        } else {
            r = storage.get(uuid);
            r.setFullName(fullName);
        }

        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (HtmlUtil.isEmpty(value)) {
                r.getContacts().remove(type);
            } else {
                r.addContact(type, value);
            }
        }
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            String[] values = request.getParameterValues(type.name());
            if (HtmlUtil.isEmpty(value) && values.length < 2) {
                r.getSections().remove(type);
            } else {
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL:
                        r.addSectionItem(type, new TextSection(value));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        r.addSectionItem(type, new ListSection(value.split("\\n")));
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        List<Organization> orgs = new ArrayList<>();
                        String[] urls = request.getParameterValues(type.name() + "url");
                        for (int i = 0; i < values.length; i++) {
                            String name = values[i];
                            if (!HtmlUtil.isEmpty(name)) {
                                List<Organization.Position> positions = new ArrayList<>();
                                String pfx = type.name() + i;
                                String[] startDates = request.getParameterValues(pfx + "startDate");
                                String[] endDates = request.getParameterValues(pfx + "endDate");
                                String[] titles = request.getParameterValues(pfx + "title");
                                String[] descriptions = request.getParameterValues(pfx + "description");
                                for (int j = 0; j < titles.length; j++) {
                                    if (!HtmlUtil.isEmpty(titles[j])) {
                                        positions.add(new Organization.Position(titles[j],DateUtil.parse(startDates[j]), DateUtil.parse(endDates[j]),  descriptions[j]));
                                    }
                                }
                                orgs.add(new Organization(new Link(name, urls[i]), positions));
                            }
                        }
                        r.addSectionItem(type, new OrgSection(orgs));
                        break;
                }
            }
        }
        if (isCreate) {
            storage.save(r);
        } else {
            storage.update(r);
        }
        response.sendRedirect("resume");
        }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
                r = storage.get(uuid);
                break;
            case "add":
                r = Resume.EMPTY;
                break;
            case "edit":
                r = storage.get(uuid);
                for (SectionType type : SectionType.values()) {
                    Section section = r.getSection(type);
                    switch (type) {
                        case OBJECTIVE:
                        case PERSONAL:
                            if (section == null) {
                                section = TextSection.EMPTY;
                            }
                            break;
                        case ACHIEVEMENT:
                        case QUALIFICATIONS:
                            if (section == null) {
                                section = ListSection.EMPTY;
                            }
                            break;
                        case EXPERIENCE:
                        case EDUCATION:
                            OrgSection orgSection = (OrgSection) section;
                            List<Organization> emptyFirstOrganizations = new ArrayList<>();
                            emptyFirstOrganizations.add(Organization.EMPTY);
                            if (orgSection != null) {
                                for (Organization org : orgSection.getOrganizations()) {
                                    List<Organization.Position> emptyFirstPositions = new ArrayList<>();
                                    emptyFirstPositions.add(Organization.Position.EMPTY);
                                    emptyFirstPositions.addAll(org.getPositions());
                                    emptyFirstOrganizations.add(new Organization(org.getHomepage(), emptyFirstPositions));
                                }
                            }
                            section = new OrgSection(emptyFirstOrganizations);
                            break;
                    }
                    r.addSectionItem(type, section);
                }
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }
}



/*
* Реализация Кислина
*
*  request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        Writer writer = response.getWriter();
        writer.write(
                "<html>\n" +
                        "<head>\n" +
                        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                        "    <link rel=\"stylesheet\" href=\"css/style.css\">\n" +
                        "    <title>Список всех резюме</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<section>\n" +
                        "<table border=\"1\" cellpadding=\"8\" cellspacing=\"0\">\n" +
                        "    <tr>\n" +
                        "        <th>Имя</th>\n" +
                        "        <th>Email</th>\n" +
                        "    </tr>\n");
        for (Resume resume : storage.getAllSorted()) {
            writer.write(
                    "<tr>\n" +
                            "     <td><a href=\"resume?uuid=" + resume.getUuid() + "\">" + resume.getFullName() + "</a></td>\n" +
                            "     <td>" + resume.getContact(ContactType.EMAIL) + "</td>\n" +
                            "</tr>\n");
        }
        writer.write("</table>\n" +
                "</section>\n" +
                "</body>\n" +
                "</html>\n");
*
* */

/*
* Моя реализация
* */
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        Storage storage = Config.getInstance().getStorage();
//        request.setCharacterEncoding("UTF-8");
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/html; charset=UTF-8");
//        String name = request.getParameter("name");
//        Writer writer = response.getWriter();
//
//        if (name == null) {
//            List<Resume> list = storage.getAllSorted();
//            setHtmlFromFile(writer, HEADER);
//            for (Resume r : list) {
//                writer.write(getResumeByUuid(r));
//            }
//            setHtmlFromFile(writer, FOOTER);
//        } else {
//            setHtmlFromFile(writer, HEADER);
//            writer.write(getResumeByUuid(storage.get(name)));
//            setHtmlFromFile(writer, FOOTER);
//        }
//    }
//
//    private void setHtmlFromFile(Writer writer, File file) {
//        try (FileInputStream fstream = new FileInputStream(file);
//             BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "UTF-8"))) {
//            String strLine;
//            while ((strLine = br.readLine()) != null) {
//                writer.write(strLine);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String getResumeByUuid(Resume r) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("<center>\n" +
//                "<table border=\"1\" width=\"640\">\n" +
//                "<tr bgcolor=\"#fff2d6\">\n" +
//                "<td colspan=\"5\" height=\"30\"><center><b>Резюме " + r.getUuid() + "</b></center></td>\n" +
//                "</tr>");
//        sb.append("<tr><td><b> ФИО</b><br>" + r.getFullName() + "</td></tr>");
//        Map<ContactType, String> contacts = r.getContacts();
//        StringBuilder contactList = new StringBuilder();
//        for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
//            contactList.append(entry.getKey().getTitle() + " : " + entry.getValue());
//        }
//        sb.append("<tr><td><b> Контакты</b><br>" + contactList.toString().trim() + "</td></tr>");
//        Map<SectionType, Section> sections = r.getSections();
//        StringBuilder sectionList = new StringBuilder();
//        for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
//            sectionList.append(entry.getKey().getTitle() + " : <br>");
//            switch (entry.getKey()) {
//                case PERSONAL:
//                case OBJECTIVE:
//                    TextSection ts = (TextSection) entry.getValue();
//                    sectionList.append("<pre> - " + ts.getContent() + "</pre>");
//                    break;
//                case ACHIEVEMENT:
//                case QUALIFICATIONS:
//                    ListSection ls = (ListSection) entry.getValue();
//                    List<String> contents = ls.getAll();
//                    for (String s : contents) {
//                        sectionList.append("<pre> - " + s + "</pre>");
//                    }
//                    break;
//                case EXPERIENCE:
//                case EDUCATION:
//                    OrgSection os = (OrgSection) entry.getValue();
//                    List<Organization> organizations = os.getOrganizations();
//                    for (Organization o : organizations) {
//                        sectionList.append("<pre> - " + o.getName() + "</pre>");
//                        sectionList.append("<a> URL: " + o.getHomepage().getUrl() + "</a>");
//                        o.getPositions().forEach(sectionList::append);
//                    }
//
//                default:
//                    break;
//
//            }
//        }
//        sb.append("<tr><td><b> Информация </b><br>" + sectionList.toString().trim() + "</td></tr>");
//        sb.append("<br></table>");
//        return sb.toString();
//    }

//}
