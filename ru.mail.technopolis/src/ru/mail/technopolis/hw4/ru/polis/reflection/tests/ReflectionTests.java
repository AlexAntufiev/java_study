package ru.mail.technopolis.hw4.ru.polis.reflection.tests;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.omg.CORBA_2_3.ORB;
import org.omg.DynamicAny.DynAny;
import ru.mail.technopolis.hw4.ru.polis.reflection.ReflectionImplementor;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleAction;
import javax.annotation.Generated;
import javax.annotation.processing.Completions;
import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.plugins.bmp.BMPImageWriteParam;
import javax.imageio.stream.FileCacheImageInputStream;
import javax.management.Descriptor;
import javax.management.ImmutableDescriptor;
import javax.management.loading.PrivateClassLoader;
import javax.management.relation.RelationNotFoundException;
import javax.management.remote.rmi.RMIIIOPServerImpl;
import javax.management.remote.rmi.RMIServerImpl;
import javax.naming.ldap.LdapReferralException;
import javax.print.MultiDoc;
import javax.sql.rowset.CachedRowSet;
import javax.transaction.TransactionRequiredException;
import javax.xml.bind.Element;
import javax.xml.bind.JAXBIntrospector;
import java.io.IOException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReflectionTests extends BaseReflectionTests {

    @Test
    public void test01_constructor() throws ClassNotFoundException, NoSuchMethodException {
        assertConstructor(ReflectionImplementor.class);
    }

    @Test
    public void test02_emptyInterfaces() throws IOException {
        test(false, Element.class, PrivateClassLoader.class);
    }

    @Test
    public void test03_standardInterfaces() throws IOException {
        test(false, Accessible.class, AccessibleAction.class, Generated.class);
    }

    @Test
    public void test04_extendedInterfaces() throws IOException {
        test(false, Descriptor.class, CachedRowSet.class, DynAny.class);
    }

    @Test
    public void test05_standardNonInterfaces() throws IOException {
        test(true, void.class, String[].class, int[].class, String.class, boolean.class);
    }

    @Test
    public void test07_defaultConstructorClasses() throws IOException {
        test(false, BMPImageWriteParam.class, RelationNotFoundException.class);
    }

    @Test
    public void test08_noDefaultConstructorClasses() throws IOException {
        test(false, IIOException.class, ImmutableDescriptor.class, LdapReferralException.class);
    }

    @Test
    public void test09_ambiguousConstructorClasses() throws IOException {
        test(false, IIOImage.class);
    }

    @Test
    public void test10_utilityClasses() throws IOException {
        test(true, Completions.class);
    }

    @Test
    public void test11_finalClasses() throws IOException {
        test(true, Integer.class, String.class);
    }

    @Test
    public void test12_standardNonClasses() throws IOException {
        test(true, void.class, String[].class, int[].class, String.class, boolean.class);
    }

    @Test
    public void test13_constructorThrows() throws IOException {
        test(false, FileCacheImageInputStream.class);
    }

    @Test
    public void test14_nonPublicAbstractMethod() throws IOException {
        test(false, RMIServerImpl.class, RMIIIOPServerImpl.class);
    }

    @Test
    public void test15_inheritedNonPublicAbstractMethod() throws IOException {
        test(false, ORB.class);
    }

    @Test
    public void test16_finalTests() throws IOException {
        test(false, MultiDoc.class, TransactionRequiredException.class, JAXBIntrospector.class);
    }
}