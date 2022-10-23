package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataStreamStrategy implements StreamStrategy {

    @Override
    public void doWrite(OutputStream outputStream, Resume resume) throws IOException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
            dataOutputStream.writeUTF(resume.getUuid());
            dataOutputStream.writeUTF(resume.getFullName());

            Map<ContactType, String> contacts = resume.getContacts();
            writeWithException(contacts.entrySet(), dataOutputStream, contact -> {
                dataOutputStream.writeUTF(contact.getKey().name());
                dataOutputStream.writeUTF(contact.getValue());
            });

            Map<SectionType, Section> sections = resume.getSections();
            writeWithException(sections.entrySet(), dataOutputStream, section -> {
                SectionType sectionType = section.getKey();
                dataOutputStream.writeUTF(sectionType.name());
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                        dataOutputStream.writeUTF(((TextSection) section.getValue()).getText());
                        break;
                    case ACHIEVEMENTS:
                    case QUALIFICATIONS:
                        List<String> stringList = ((ListSection) section.getValue()).getList();
                        writeWithException(stringList, dataOutputStream, dataOutputStream::writeUTF);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Company> companyList = ((CompanyListSection) section.getValue()).getCompanies();
                        writeWithException(companyList, dataOutputStream, company -> {
                            dataOutputStream.writeUTF(company.getName());
                            dataOutputStream.writeUTF(Optional.ofNullable(company.getUrl()).orElse(""));
                            List<Company.Period> periods = company.getPeriods();
                            writeWithException(periods, dataOutputStream, period -> {
                                dataOutputStream.writeUTF(period.getTitle());
                                dataOutputStream.writeUTF(Optional.ofNullable(period.getDescription()).orElse(""));
                                dataOutputStream.writeUTF(period.getBeginDate().toString());
                                dataOutputStream.writeUTF(period.getEndDate().toString());
                            });
                        });
                        break;
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream inputStream) throws Exception {
        try (DataInputStream dataInputStream = new DataInputStream(inputStream)) {
            Resume resume = new Resume(dataInputStream.readUTF(), dataInputStream.readUTF());

            readWithException(dataInputStream, () -> {
                resume.addContact(ContactType.valueOf(dataInputStream.readUTF()), dataInputStream.readUTF());
            });

            readWithException(dataInputStream, () -> {
                SectionType sectionType = SectionType.valueOf(dataInputStream.readUTF());
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                        TextSection textSection = new TextSection();
                        textSection.setText(dataInputStream.readUTF());
                        resume.addSection(sectionType, textSection);
                        break;
                    case ACHIEVEMENTS:
                    case QUALIFICATIONS:
                        ListSection listSection = new ListSection();
                        readWithException(dataInputStream, () -> {
                            listSection.addLine(dataInputStream.readUTF());
                        });
                        resume.addSection(sectionType, listSection);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        CompanyListSection companyListSection = new CompanyListSection();
                        readWithException(dataInputStream, () -> {
                            String companyName = dataInputStream.readUTF();
                            String companyUrl = dataInputStream.readUTF();
                            Company company = new Company(companyName, companyUrl.isEmpty() ? null : companyUrl);
                            readWithException(dataInputStream, () -> {
                                String periodTitle = dataInputStream.readUTF();
                                String periodDescription = dataInputStream.readUTF();
                                company.addPeriod(new Company.Period(periodTitle,
                                        periodDescription.isEmpty() ? null : periodDescription,
                                        LocalDate.parse(dataInputStream.readUTF()),
                                        LocalDate.parse(dataInputStream.readUTF())));
                            });
                            companyListSection.addCompany(company);
                        });
                        resume.addSection(sectionType, companyListSection);
                        break;
                }
            });
            return resume;
        }
    }

    @FunctionalInterface
    private interface ConsumerWithException<T> {
        void accept(T t) throws IOException;
    }

    private static <T> void writeWithException(Collection<? extends T> collection,
                                               DataOutputStream dataOutputStream,
                                               ConsumerWithException<? super T> action) throws IOException {
        Objects.requireNonNull(collection);
        Objects.requireNonNull(dataOutputStream);
        Objects.requireNonNull(action);
        dataOutputStream.writeInt(collection.size());
        for (T t : collection) {
            action.accept(t);
        }
    }

    @FunctionalInterface
    private interface RunnableWithException {
        void run() throws IOException;
    }

    private static void readWithException(DataInputStream dataInputStream,
                                          RunnableWithException action) throws IOException {
        Objects.requireNonNull(dataInputStream);
        Objects.requireNonNull(action);
        int counter = dataInputStream.readInt();
        for (; counter > 0; --counter) {
            action.run();
        }
    }
}
