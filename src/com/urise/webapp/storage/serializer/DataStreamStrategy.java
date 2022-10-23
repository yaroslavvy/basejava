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

            int contactCounter = dataInputStream.readInt();
            for (; contactCounter > 0; --contactCounter) {
                resume.addContact(ContactType.valueOf(dataInputStream.readUTF()), dataInputStream.readUTF());
            }

            int sectionCounter = dataInputStream.readInt();
            for (; sectionCounter > 0; --sectionCounter) {
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
                        int lineCounter = dataInputStream.readInt();
                        ListSection listSection = new ListSection();
                        for (; lineCounter > 0; --lineCounter) {
                            listSection.addLine(dataInputStream.readUTF());
                        }
                        resume.addSection(sectionType, listSection);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        int companyCounter = dataInputStream.readInt();
                        CompanyListSection companyListSection = new CompanyListSection();
                        for (; companyCounter > 0; --companyCounter) {
                            String companyName = dataInputStream.readUTF();
                            String companyUrl = dataInputStream.readUTF();
                            Company company = new Company(companyName, companyUrl.isEmpty() ? null : companyUrl);
                            int periodCounter = dataInputStream.readInt();
                            for (; periodCounter > 0; --periodCounter) {
                                String periodTitle = dataInputStream.readUTF();
                                String periodDescription = dataInputStream.readUTF();
                                company.addPeriod(new Company.Period(periodTitle,
                                        periodDescription.isEmpty() ? null : periodDescription,
                                        LocalDate.parse(dataInputStream.readUTF()),
                                        LocalDate.parse(dataInputStream.readUTF())));
                            }
                            companyListSection.addCompany(company);
                        }
                        resume.addSection(sectionType, companyListSection);
                        break;
                }
            }
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
}
