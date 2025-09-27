package com.example.motivacionalapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Actividad principal de la aplicación Generador de Frases de Motivación
 * Muestra frases motivacionales en español seleccionadas aleatoriamente
 */
public class MainActivity extends AppCompatActivity {

    // Elementos de la interfaz de usuario
    private TextView tvQuote;
    private TextView tvAuthor;
    private Button btnNewQuote;
    private ProgressBar progressBar;
    private TextView tvAttribution;

    // Array de frases motivacionales en español
    private static final String[][] MOTIVATIONAL_QUOTES = {
        {"El éxito es la suma de pequeños esfuerzos repetidos día tras día.", "Robert Collier"},
        {"No te preocupes por los fracasos, preocúpate por las oportunidades que pierdes cuando ni siquiera lo intentas.", "Jack Canfield"},
        {"El futuro pertenece a quienes creen en la belleza de sus sueños.", "Eleanor Roosevelt"},
        {"La única manera de hacer un gran trabajo es amar lo que haces.", "Steve Jobs"},
        {"No hay nada imposible, la palabra misma dice 'soy posible'.", "Audrey Hepburn"},
        {"La vida es lo que pasa mientras estás ocupado haciendo otros planes.", "John Lennon"},
        {"Sé tú mismo; todas las demás personas ya están tomadas.", "Oscar Wilde"},
        {"Dos caminos divergieron en un bosque y yo tomé el menos transitado, y eso marcó toda la diferencia.", "Robert Frost"},
        {"Siempre parece imposible hasta que se hace.", "Nelson Mandela"},
        {"La vida es 10% lo que te pasa y 90% cómo reaccionas ante ello.", "Charles R. Swindoll"},
        {"El mejor momento para plantar un árbol fue hace 20 años. El segundo mejor momento es ahora.", "Proverbio chino"},
        {"No esperes el momento perfecto, toma el momento y hazlo perfecto.", "Anónimo"},
        {"El éxito no es la clave de la felicidad. La felicidad es la clave del éxito.", "Albert Schweitzer"},
        {"No cuentes los días, haz que los días cuenten.", "Muhammad Ali"},
        {"La única persona que estás destinado a convertirte es la persona que decides ser.", "Ralph Waldo Emerson"},
        {"La vida es una aventura atrevida o no es nada.", "Helen Keller"},
        {"No puedes usar la creatividad. Cuanto más usas, más tienes.", "Maya Angelou"},
        {"La mejor venganza es el éxito masivo.", "Frank Sinatra"},
        {"El único lugar donde el éxito viene antes que el trabajo es en el diccionario.", "Vidal Sassoon"},
        {"La innovación distingue entre un líder y un seguidor.", "Steve Jobs"},
        {"No hay ascensor al éxito. Tienes que tomar las escaleras.", "Zig Ziglar"},
        {"La confianza en sí mismo es el primer secreto del éxito.", "Ralph Waldo Emerson"},
        {"El fracaso es simplemente la oportunidad de comenzar de nuevo, esta vez de manera más inteligente.", "Henry Ford"},
        {"La educación es el arma más poderosa que puedes usar para cambiar el mundo.", "Nelson Mandela"},
        {"Si puedes soñarlo, puedes hacerlo.", "Walt Disney"},
        {"La mejor manera de predecir el futuro es crearlo.", "Peter Drucker"},
        {"No tengas miedo de renunciar a lo bueno para ir tras lo grandioso.", "John D. Rockefeller"},
        {"La felicidad no es algo que pospones para el futuro; es algo que diseñas para el presente.", "Jim Rohn"},
        {"La única manera de hacer un gran trabajo es amar lo que haces.", "Steve Jobs"},
        {"Tu tiempo es limitado, no lo desperdicies viviendo la vida de otra persona.", "Steve Jobs"}
    };
    
    // ExecutorService para manejar operaciones en hilos secundarios
    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        // Configurar el sistema de insets para manejar la barra de estado
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar elementos de la interfaz
        initializeViews();
        
        // Configurar el executor para operaciones de red
        executor = Executors.newSingleThreadExecutor();
        
        // Configurar el listener del botón
        setupButtonListener();
        
        // Configurar el listener del texto de atribución
        setupAttributionListener();
        
        // Cargar la primera frase al iniciar la aplicación
        loadNewQuote();
    }

    /**
     * Inicializa las referencias a los elementos de la interfaz de usuario
     */
    private void initializeViews() {
        tvQuote = findViewById(R.id.tvQuote);
        tvAuthor = findViewById(R.id.tvAuthor);
        btnNewQuote = findViewById(R.id.btnNewQuote);
        progressBar = findViewById(R.id.progressBar);
        tvAttribution = findViewById(R.id.tvAttribution);
    }

    /**
     * Configura el listener del botón "Nueva frase"
     */
    private void setupButtonListener() {
        btnNewQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNewQuote();
            }
        });
    }

    /**
     * Configura el listener del texto de atribución (ahora solo informativo)
     */
    private void setupAttributionListener() {
        // El texto de atribución ahora es solo informativo, no clickeable
        tvAttribution.setClickable(false);
        tvAttribution.setFocusable(false);
        tvAttribution.setBackground(null);
    }


    /**
     * Carga una nueva frase motivacional desde la lista local
     */
    private void loadNewQuote() {
        // Mostrar indicador de carga y deshabilitar el botón
        showLoading(true);

        // Simular un pequeño delay para mostrar el loading (opcional)
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Pequeño delay para simular carga (opcional)
                    Thread.sleep(500);
                    
                    // Obtener una frase aleatoria
                    displayRandomQuote();
                    
                } catch (Exception e) {
                    // Manejar errores en el hilo principal
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showError();
                        }
                    });
                }
            }
        });
    }

    /**
     * Muestra una frase aleatoria de la lista local
     */
    private void displayRandomQuote() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Generar un índice aleatorio
                    int randomIndex = (int) (Math.random() * MOTIVATIONAL_QUOTES.length);
                    
                    // Obtener la frase y autor
                    String quote = MOTIVATIONAL_QUOTES[randomIndex][0];
                    String author = MOTIVATIONAL_QUOTES[randomIndex][1];
                    
                    // Actualizar la interfaz
                    tvQuote.setText(quote);
                    tvAuthor.setText("— " + author);
                    showLoading(false);
                    
                } catch (Exception e) {
                    showError();
                }
            }
        });
    }


    /**
     * Muestra o oculta el indicador de carga y habilita/deshabilita el botón
     * @param isLoading true para mostrar carga, false para ocultarla
     */
    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnNewQuote.setEnabled(false);
            btnNewQuote.setText("Cargando...");
        } else {
            progressBar.setVisibility(View.GONE);
            btnNewQuote.setEnabled(true);
            btnNewQuote.setText("Nueva frase");
        }
    }

    /**
     * Muestra un mensaje de error y restaura el estado normal de la interfaz
     */
    private void showError() {
        showLoading(false);
        Toast.makeText(this, "No se pudo cargar la frase. Verifica tu conexión.", Toast.LENGTH_LONG).show();
        
        // Mostrar mensaje de fallback
        tvQuote.setText("No se pudo cargar una nueva frase. Presiona el botón para intentar nuevamente.");
        tvAuthor.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cerrar el executor cuando la actividad se destruye
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}