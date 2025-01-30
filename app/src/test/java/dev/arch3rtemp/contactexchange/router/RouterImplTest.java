package dev.arch3rtemp.contactexchange.router;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RouterImplTest {

    @Rule
    public MockitoRule mockRule = MockitoJUnit.rule();
    @Mock
    private Fragment fragment;
    @Mock
    private FragmentTransaction fragmentTransaction;
    @Mock
    private FragmentManager fragmentManager;
    @InjectMocks
    private RouterImpl router;

    @Before
    public void setUp() {
        when(fragmentManager.beginTransaction()).thenReturn(fragmentTransaction);
        when(fragmentTransaction.setCustomAnimations(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(fragmentTransaction);
        when(fragmentTransaction.replace(anyInt(), any(), anyString())).thenReturn(fragmentTransaction);
        when(fragmentTransaction.addToBackStack(any())).thenReturn(fragmentTransaction);
        when(fragmentTransaction.commit()).thenReturn(1);
    }

    @Test
    public void navigate_callsCorrectMethods() {
        router.navigate(fragment, true, true);

        verify(fragmentManager).beginTransaction();
        verify(fragmentTransaction).setCustomAnimations(anyInt(), anyInt(), anyInt(), anyInt());
        verify(fragmentTransaction).replace(anyInt(), any(), anyString());
        verify(fragmentTransaction).addToBackStack(any());
        verify(fragmentTransaction).commit();
    }

    @Test
    public void navigate_callsCorrectMethodsWithoutAnimation() {
        router.navigate(fragment, false, true);

        verify(fragmentManager).beginTransaction();
        verify(fragmentTransaction, never()).setCustomAnimations(anyInt(), anyInt(), anyInt(), anyInt());
        verify(fragmentTransaction).replace(anyInt(), any(), anyString());
        verify(fragmentTransaction).addToBackStack(any());
        verify(fragmentTransaction).commit();
    }

    @Test
    public void navigate_callsCorrectMethodsWithoutBackstack() {
        router.navigate(fragment, true, false);

        verify(fragmentManager).beginTransaction();
        verify(fragmentTransaction).setCustomAnimations(anyInt(), anyInt(), anyInt(), anyInt());
        verify(fragmentTransaction).replace(anyInt(), any(), anyString());
        verify(fragmentTransaction, never()).addToBackStack(any());
        verify(fragmentTransaction).commit();
    }
}
